package br.com.fiap.Portaria.controller;

import br.com.fiap.Portaria.dto.FirebaseLoginRequestDTO;
import br.com.fiap.Portaria.dto.FirebaseRegisterRequestDTO;
import br.com.fiap.Portaria.entity.Usuario;
import br.com.fiap.Portaria.repository.UsuarioRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Login e registro via Firebase — não exigem token no header")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @Operation(summary = "Login com Firebase", description = "Envia o idToken do Firebase e recebe os dados do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Token Firebase inválido")
    @ApiResponse(responseCode = "404", description = "Email não cadastrado no sistema")
    @PostMapping("/firebase-login")
    public ResponseEntity<?> firebaseLogin(@RequestBody FirebaseLoginRequestDTO body) {
        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(body.getToken());
            String email = firebaseToken.getEmail();
            String uid = firebaseToken.getUid();

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            Usuario usuario;
            if (usuarioOpt.isEmpty()) {
                // Se o usuário foi criado no Firebase mas não existe no SQL (ex: criado pelo painel Super Admin), cria ele automaticamente!
                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setFirebaseUid(uid);
                
                // Mapeia automaticamente o perfil (ADMIN ou MORADOR) com regras estritas
                if (email.toLowerCase().contains("@admin") || email.toLowerCase().contains("@porteiro")) {
                    usuario.setPerfil(br.com.fiap.Portaria.dto.enums.Role.ADMIN);
                } else {
                    usuario.setPerfil(br.com.fiap.Portaria.dto.enums.Role.MORADOR);
                }
                
                // Busca o próximo ID disponível para ID_USUARIO
                jakarta.persistence.Query query = entityManager.createNativeQuery("SELECT NVL(MAX(ID_USUARIO), 0) + 1 FROM TPL_USUARIO");
                Integer proximoId = ((Number) query.getSingleResult()).intValue();
                usuario.setIdUsuario(proximoId);
                
                usuario = usuarioRepository.save(usuario);
            } else {
                usuario = usuarioOpt.get();
                // vincula o firebaseUid se ainda não tiver
                if (usuario.getFirebaseUid() == null) {
                    usuario.setFirebaseUid(uid);
                    usuarioRepository.save(usuario);
                }
            }

            return ResponseEntity.ok(Map.of(
                    "uid", uid,
                    "email", email,
                    "user", Map.of(
                            "id", usuario.getIdUsuario(),
                            "perfil", usuario.getPerfil().name(),
                            "idMorador", usuario.getIdMorador() != null ? usuario.getIdMorador() : "",
                            "idPortaria", usuario.getIdPortaria() != null ? usuario.getIdPortaria() : ""
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("erro", "Token Firebase inválido"));
        }
    }

    @Operation(summary = "Registro com Firebase", description = "Primeiro acesso — verifica se email existe no sistema e vincula o Firebase UID")
    @ApiResponse(responseCode = "200", description = "Conta vinculada com sucesso")
    @ApiResponse(responseCode = "400", description = "Conta já foi ativada anteriormente")
    @ApiResponse(responseCode = "401", description = "Token Firebase inválido")
    @ApiResponse(responseCode = "404", description = "Email não cadastrado no sistema")
    @PostMapping("/firebase-register")
    public ResponseEntity<?> firebaseRegister(@RequestBody FirebaseRegisterRequestDTO body) {
        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(body.getToken());
            String email = firebaseToken.getEmail();
            String uid = firebaseToken.getUid();

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            Usuario usuario;
            if (usuarioOpt.isEmpty()) {
                // Se o usuário foi criado no Firebase mas não existe no SQL, cria ele automaticamente!
                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setFirebaseUid(uid);
                
                // Mapeia automaticamente o perfil (ADMIN ou MORADOR) com regras estritas
                if (email.toLowerCase().contains("@admin") || email.toLowerCase().contains("@porteiro")) {
                    usuario.setPerfil(br.com.fiap.Portaria.dto.enums.Role.ADMIN);
                } else {
                    usuario.setPerfil(br.com.fiap.Portaria.dto.enums.Role.MORADOR);
                }
                
                jakarta.persistence.Query query = entityManager.createNativeQuery("SELECT NVL(MAX(ID_USUARIO), 0) + 1 FROM TPL_USUARIO");
                Integer proximoId = ((Number) query.getSingleResult()).intValue();
                usuario.setIdUsuario(proximoId);
                
                usuario = usuarioRepository.save(usuario);
            } else {
                usuario = usuarioOpt.get();
                if (usuario.getFirebaseUid() != null) {
                    return ResponseEntity.badRequest().body(Map.of("erro", "Conta já foi ativada anteriormente"));
                }
                usuario.setFirebaseUid(uid);
                usuarioRepository.save(usuario);
            }

            return ResponseEntity.ok(Map.of(
                    "uid", uid,
                    "email", email,
                    "user", Map.of(
                            "id", usuario.getIdUsuario(),
                            "perfil", usuario.getPerfil().name(),
                            "idMorador", usuario.getIdMorador() != null ? usuario.getIdMorador() : "",
                            "idPortaria", usuario.getIdPortaria() != null ? usuario.getIdPortaria() : ""
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("erro", "Token Firebase inválido"));
        }
    }
}
