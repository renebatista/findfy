package com.projetoa3.api.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaiimageio.jpeg2000.J2KImageWriteParam;
import com.projetoa3.api.entities.Avistamento;
import com.projetoa3.api.entities.Desaparecido;
import com.projetoa3.api.service.AvistamentoService;
import com.projetoa3.api.service.DesaparecidoService;

@RestController
@RequestMapping("/api-a3")
public class Controller {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DesaparecidoService desaparecidoService;

    @Autowired
    private AvistamentoService avistamentoService;

    @GetMapping("/testarConexaoDb")
    public String testeConexao() {
        StringBuilder response = new StringBuilder();
        try {
            if (dataSource != null && dataSource.getConnection() != null) {
                response.append("Conexão com o banco de dados: OK\n");
            } else {
                response.append("Não foi possível estabelecer conexão com o banco de dados\n");
            }
        } catch (SQLException e) {
            response.append("Erro ao obter conexão com o banco de dados: ").append(e.getMessage()).append("\n");
        }

        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
            response.append("Tabelas encontradas no banco de dados:\n");
            while (tables.next()) {
                response.append(tables.getString("TABLE_NAME")).append("\n");
            }
        } catch (SQLException e) {
            response.append("Erro ao obter lista de tabelas: ").append(e.getMessage()).append("\n");
        }

        return response.toString();
    }

    @PostMapping(value = "/cadastrarDesaparecido", consumes = "multipart/form-data")
    public ResponseEntity<Desaparecido> createDesaparecido(
            @RequestParam String desaparecidoJson,
            @RequestParam(value = "imagem", required = false) MultipartFile arquivo
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Desaparecido desaparecido = objectMapper.readValue(desaparecidoJson, Desaparecido.class);

            if (arquivo != null && !arquivo.isEmpty()) {
                var imagemComprimida = comprimirImagem(arquivo);

                desaparecido.setImagem(imagemComprimida);
                System.out.println("Imagem comprimida recebida. Tamanho: " + imagemComprimida.length + " bytes");
            } else {
                System.out.println("Nenhuma imagem enviada");
            }

            Desaparecido desaparecidoSalvo = desaparecidoService.createDesaparecido(desaparecido);

            return new ResponseEntity<>(desaparecidoSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar desaparecido: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método para comprimir imagem, fazendo-a ocupar menos espaço no DB
    
    public byte[] comprimirImagem(MultipartFile arquivo) throws Exception {
    try {
        BufferedImage imagem = ImageIO.read(arquivo.getInputStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // Ajuste o parâmetro de compressão conforme necessário
        float compressao = 0.001f; // Exemplo de compressão (0.1f é uma qualidade baixa)
        
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = writers.next();
        
        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(outputStream);
        writer.setOutput(imageOutput);
        
        // Parâmetros para compressão JPEG
        ImageWriteParam params = writer.getDefaultWriteParam();
        
        // Define a qualidade da compressão
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(compressao);
        
        // Escreve a imagem com os parâmetros configurados
        writer.write(null, new IIOImage(imagem, null, null), params);
        
        writer.dispose();
        imageOutput.close();
        
        return outputStream.toByteArray();
    } catch (Exception e) {
        throw new Exception("Erro ao comprimir imagem", e);
    }
}

    


    @PostMapping("/adicionarAvistamento")
    public void createAvistamento(@RequestParam Long id, @RequestParam String comentario) throws Exception {
        try {
            avistamentoService.createAvistamento(id, comentario);
        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar avistamento.", e);
        }
    }

    @GetMapping("/listarAvistamentosPorIdDesaparecido")
    public List<String> puxarAvistamentos(@RequestParam Long desaparecidoId) throws Exception {
        try {
            List<String> listaAvistamentos = avistamentoService.getAvistamentosByDesaparecidoId(desaparecidoId);

            return listaAvistamentos;
        } catch (Exception e) {
            throw new Exception("Erro ao buscar avistamentos", e);
        }
    }

    @GetMapping("/listarDesaparecidos")
    public List<Desaparecido> getAllDesaparecidos() throws Exception{
        try {
            List<Desaparecido> desaparecido = desaparecidoService.getAllDesaparecidos();

            for (Desaparecido sumido : desaparecido) {
                sumido.setImagem(null);
            }

            return desaparecido;
        } catch (Exception e) {
            throw new Exception("Erro ao listar os desaparecidos.", e);
        }
    }


    @GetMapping(value = "/puxarFotoDesaparecido/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getFotoDesaparecido(@PathVariable int id) throws Exception {
        try {
            Desaparecido desaparecido = desaparecidoService.getDesaparecidoById(id);
            if (desaparecido != null && desaparecido.getImagem() != null) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(desaparecido.getImagem());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new Exception("Erro ao puxar foto do desaparecido", e);
        }
    }

    
    @GetMapping("/PuxarDesaparecidoPorId/{id}")
    public Desaparecido getDesaparecidoById(@PathVariable int id) throws Exception {
        try {
            Desaparecido desaparecido = desaparecidoService.getDesaparecidoById(id);
            if (desaparecido != null) {
                desaparecido.setImagem(null);
            }
            
            return desaparecido;
        } catch (Exception e) {
            System.out.println("------------=[ DEU RUIM, CAIU NO CATCH ]=-----------\n\n\n" + e);
            throw new Exception("Ocorreu um erro ao obter o desaparecido por ID.", e);
        }
    }

    @PutMapping("/atualizarDesaparecido/{id}")
    public ResponseEntity<Desaparecido> updateDesaparecido(@PathVariable int id, @RequestBody Desaparecido desaparecido) throws Exception {
        try {
            Desaparecido atualizado = desaparecidoService.updateDesaparecido(id, desaparecido);

            return new ResponseEntity<>(atualizado, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar desaparecido", e);
        }
    }

    @DeleteMapping("/deletarDesaparecido/{id}")
    public ResponseEntity<Void> deleteDesaparecido(@PathVariable int id) throws Exception {
        try {
            boolean desaparecidoDeletado = desaparecidoService.deleteDesaparecido(id);
            if (desaparecidoDeletado) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new Exception("Erro ao deletar usuário.", e);
        }
    }

}
