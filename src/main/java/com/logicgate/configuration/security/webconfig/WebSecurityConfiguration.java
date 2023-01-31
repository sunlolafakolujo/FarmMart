package com.logicgate.configuration.security.webconfig;



import com.logicgate.configuration.appuserdetailservice.AppUserDetailService;
import com.logicgate.configuration.security.authentrypoint.JwtAuthenticationEntryPoint;
import com.logicgate.configuration.security.jwtrequestfilter.JwtRequestFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.function.ServerRequest;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AppUserDetailService appUserDetailService;
    private final PasswordEncoder passwordEncode;
    private static final String[] WHITE_LIST_URLS={
            "/api/farmmart/addRole/**", "/api/farmmart/addSeller/**",
            "/api/farmmart/addBuyer/**", "/api/farmmart/findProduct/**", "/api/farmmart/findByProductWithinPriceRange/**",
            "/api/farmmart/findAllProductOrByTypeOrNameOrBrandOrPartNumber/**", "/api/farmmart/findProductByCategory",
            "/api/farmmart/findProductByPriceWithinPriceLimit/**", "/api/farmmart/verifyRegistration/**",
            "/api/farmmart/resendVerificationToken/**","/api/farmmart/addEmployee/**",
            "/api/farmmart/findUserByUsernameOrEmailOrMobile/**", "/api/farmmart/logIn/**",
            "/api/farmmart/findByInvoiceNumber/**", "/api/farmmart/findByInvoiceNumber/**", "/api/farmmart/findAllProducts/**"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(appUserDetailService).passwordEncoder(passwordEncode);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable()
                .authorizeRequests()
                .mvcMatchers(WHITE_LIST_URLS).permitAll()
                .mvcMatchers(HttpHeaders.ALLOW).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter(){
        return new JwtRequestFilter();
    }
}
