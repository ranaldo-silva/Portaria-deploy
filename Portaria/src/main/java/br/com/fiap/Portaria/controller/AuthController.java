package br.com.fiap.Portaria.controller;

import br.com.fiap.Portaria.dto.FirebaseRegisterRequestDTO;
import br.com.fiap.Portaria.dto.enums.Role;
import br.com.fiap.Portaria.entity.Usuario;
import br.com.fiap.Portaria.repository.UsuarioRepository;
import br.com.fiap.Portaria.service.MoradorService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MoradorService moradorService;

    @PostMapping("/firebase-login")
    public ResponseEntity<?> firebaseLogin(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null) return ResponseEntity.badRequest().body("Token não fornecido.");

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String email = decodedToken.getEmail();

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                // Se já existir, retorna as informações dele
                return ResponseEntity.ok(Map.of(
                        "message", "Login bem-sucedido via Firebase.",
                        "user", Map.of(
                                "id", usuario.getIdUsuario(),
                                "perfil", usuario.getPerfil().name(),
                                "idMorador", usuario.getIdMorador() != null ? usuario.getIdMorador() : "",
                                "email", usuario.getEmail()
                        )
                ));
            } else {
                return ResponseEntity.status(404).body("Usuário verificado pelo Firebase, mas não encontrado no banco relacional da portaria.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Token Firebase inválido: " + e.getMessage());
        }
    }

    @PostMapping("/firebase-register")
    public ResponseEntity<?> firebaseRegister(@RequestBody FirebaseRegisterRequestDTO body) {
        String token = body.getToken();
        if (token == null) return ResponseEntity.badRequest().body("Token não fornecido.");

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String email = decodedToken.getEmail();
            String uid = decodedToken.getUid();

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Usuário já existe no banco relacional.");
            } else {
                // Cria novo usuário (A entidade Usuario tem apenas id, email, firebaseUid, perfil, idMorador, idPortaria)
                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setFirebaseUid(uid);

                // REGRAS DE SEGURANÇA BASEADAS NO E-MAIL:
                if (email.contains("@admin") || email.contains("@porteiro")) {
                    usuario.setPerfil(Role.ADMIN);
                } else {
                    usuario.setPerfil(Role.MORADOR);
                }

                // 2. Se for Morador, criamos a entidade MORADOR no banco Oracle automaticamente!
                if (usuario.getPerfil() == Role.MORADOR) {
                    if (body.getTelefone() != null && body.getApartamentoId() != null) {
                        try {
                            br.com.fiap.Portaria.dto.MoradorRequestDTO moradorDTO = new br.com.fiap.Portaria.dto.MoradorRequestDTO();
                            moradorDTO.setNome(body.getNome() != null && !body.getNome().isBlank() ? body.getNome() : "Morador Registrado");
                            moradorDTO.setEmail(email);
                            moradorDTO.setTelefone(body.getTelefone());
                            moradorDTO.setBloco(body.getBloco() != null && !body.getBloco().isBlank() ? body.getBloco() : "A");

                            // Converte o apartamento de forma segura
                            try {
                                moradorDTO.setApartamentoId(Integer.parseInt(body.getApartamentoId().replaceAll("[^0-9]", "")));
                            } catch (NumberFormatException e) {
                                moradorDTO.setApartamentoId(1); // fallback de segurança
                            }
                            
                            br.com.fiap.Portaria.dto.MoradorResponseDTO moradorCriado = moradorService.salvar(moradorDTO);
                            usuario.setIdMorador(moradorCriado.getId());
                        } catch (Exception ex) {
                            System.err.println("Erro ao criar morador no registro: " + ex.getMessage());
                        }
                    }
                }

                usuarioRepository.save(usuario);

                return ResponseEntity.ok(Map.of(
                        "message", "Usuário criado com sucesso no banco relacional.",
                        "user", Map.of(
                                "id", usuario.getIdUsuario() != null ? usuario.getIdUsuario() : 0,
                                "perfil", usuario.getPerfil().name(),
                                "idMorador", usuario.getIdMorador() != null ? usuario.getIdMorador() : "",
                                "email", usuario.getEmail()
                        )
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Erro na validação Firebase: " + e.getMessage());
        }
    }
}
