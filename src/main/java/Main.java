import config.EntityConfiguration;
import config.ServiceConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.base.Operator;

public class Main {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EntityConfiguration.class);
        context.register(ServiceConfiguration.class);
        context.refresh();
        final Operator operator = (Operator) context.getBean("operator");
//        String path = "E:\\projects\\parser-CSV-specification\\src\\main\\resources\\table.csv";
//        operator.doWork(path);
        operator.doWork(args[0]);
    }
}
