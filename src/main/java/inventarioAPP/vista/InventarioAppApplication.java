package inventarioAPP.vista;

import inventarioAPP.controlador.ControladorProducto;
import inventarioAPP.modelo.RepositorioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@ComponentScan("inventarioAPP.modelo")
@EnableJdbcRepositories("inventarioAPP.modelo")
public class InventarioAppApplication {
    @Autowired
    RepositorioProducto repositorioProducto;
	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(InventarioAppApplication.class);
                builder.headless(false);
                ConfigurableApplicationContext context = builder.run(args);
	}

    @Bean
    ApplicationRunner applicationRunner(){
        return args -> {
            Vista vista = new Vista();
            VistaActualizar vistaActualizar = new VistaActualizar();
            ControladorProducto controlador = new ControladorProducto(repositorioProducto, vista, vistaActualizar);
            vista.setControlador(controlador);
            vistaActualizar.setControlador(controlador);
        };
    }
}
