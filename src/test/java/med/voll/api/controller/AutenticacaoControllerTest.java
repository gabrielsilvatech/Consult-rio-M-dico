package med.voll.api.controller;

import med.voll.api.domain.usuario.DadosAutenticacao;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DadosAutenticacao> dadosAutenticacaoJacksonTester;
    @MockBean
    private AuthenticationManager manager;
    @MockBean
    private TokenService tokenService;
    @Autowired
    private JacksonTester<DadosTokenJWT> dadosTokenJWTJacksonTester;

    @Test
    @WithMockUser
    @DisplayName("Deve devolver código http 200 quando as credenciais forem válidas, e devolve um tokenJWT no corpo da requisição")
    void efetuarLoginCenario1() throws Exception {
        var dados = new DadosAutenticacao("medico.test@voll.med", "123456");
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var token = "mockTest";
        Authentication authentication = mock(Authentication.class);
        var usuario = (Usuario) authentication.getPrincipal();

        when(manager.authenticate(authenticationToken)).thenReturn(authentication);
        when(tokenService.gerarToken(usuario)).thenReturn(token);

        var response = mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAutenticacaoJacksonTester.write(dados).getJson()))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosTokenJWTJacksonTester.write(new DadosTokenJWT(token)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @WithMockUser
    @DisplayName("Deve devolver código http 401, quando as credenciais forem inválidas")
    void efetuarLoginCenario2() throws Exception {
        var dados = new DadosAutenticacao(null, "123456");
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());

        when(manager.authenticate(authenticationToken)).thenThrow(BadCredentialsException.class);

        var response = mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAutenticacaoJacksonTester.write(dados).getJson()))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}