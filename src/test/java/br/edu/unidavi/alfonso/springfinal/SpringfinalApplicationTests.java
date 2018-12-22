package br.edu.unidavi.alfonso.springfinal;

import br.edu.unidavi.alfonso.springfinal.db.entity.Produto;
import br.edu.unidavi.alfonso.springfinal.db.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringfinalApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonParser;

    @Autowired
    ProdutoRepository repoProduto;

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "password");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("password","secret"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @Test
    public void testPostProduto() throws Exception {
        String accessToken = obtainAccessToken("root", "t0ps3cr3t");

        Date data = null;
        try {
            data = (Date)formatter.parse("2018-12-20");
        } catch (ParseException e) {
            data = new Date();
        }

        Produto produto = new Produto();
        produto.setId(0L);
        produto.setNome("PRODUTO1");
        produto.setDescricao("DESCRIÇÃO DO PRODUTO 1");
        produto.setValor(10.5);
        produto.setMarca("MARCA");
        produto.setDataCriacao(data);

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonParser.writeValueAsString(produto))
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetAll() throws Exception {
        String accessToken = obtainAccessToken("root", "t0ps3cr3t");

        MvcResult res = mockMvc.perform(
                    get("/produtos")
                        .header("Authorization", "Bearer " + accessToken)
                ).andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("$._embedded.produtoes.[0].id", equalTo(1)))
                .andExpect(jsonPath("$._embedded.produtoes.[0].nome", equalTo("PRODUTO1")))
                .andReturn();

    }

}
