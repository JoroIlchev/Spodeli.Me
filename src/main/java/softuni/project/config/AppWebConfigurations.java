package softuni.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import softuni.project.web.interceptors.BackGroundImgInterceptor;
import softuni.project.web.interceptors.TitleInterceptor;

@Configuration
public class AppWebConfigurations implements WebMvcConfigurer {

    private final TitleInterceptor titleInterceptor;
    private final BackGroundImgInterceptor backGroundImgInterceptor;

    @Autowired
    public AppWebConfigurations(TitleInterceptor titleInterceptor, BackGroundImgInterceptor backGroundImgInterceptor) {
        this.titleInterceptor = titleInterceptor;
        this.backGroundImgInterceptor = backGroundImgInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(titleInterceptor);
        registry.addInterceptor(backGroundImgInterceptor);
    }
}
