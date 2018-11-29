package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoisonEnricherProcessor implements Processor {
    private Integer concentration;

    @Override
    public void process(Exchange exchange) throws Exception {
        List bodyAsList = exchange.getIn().getBody(List.class);
        if (bodyAsList != null) {
            Map<String, Long> body = (Map)bodyAsList.get(0);
            if (body != null) {
                Map newBody = new HashMap();
                newBody.put("personId", body.get("personId"));
                newBody.put("concentration", this.concentration);
                exchange.getOut().setBody(newBody);
                exchange.getOut().setHeaders(exchange.getIn().getHeaders());
            }
        }
    }

    public Integer getConcentration() {
        return concentration;
    }

    public void setConcentration(Integer concentration) {
        this.concentration = concentration;
    }
}
