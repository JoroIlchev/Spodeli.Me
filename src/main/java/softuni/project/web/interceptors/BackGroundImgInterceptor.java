package softuni.project.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Component
public class BackGroundImgInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String url = "";
        LocalDate localDate = LocalDate.now();
        int month = localDate.getMonth().getValue();

        if (month == 12 || month <= 2) {
            url = "/img/winter.png";
        } else if (month > 2 && month <= 5) {
            url = "/img/spring.png";
        } else if (month > 5 && month <= 8) {
            url = "/img/summer.png";
        } else if (month > 8 && month <= 11) {
            url = "/img/autumn.png";
        }

        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        } else {
            if (handler instanceof HandlerMethod) {
                modelAndView
                        .addObject("url", url);
            }
        }
    }
}
