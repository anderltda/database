package br.com.process.integration.database.core.ui.genesis;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WellKnownController {

    @GetMapping("/.well-known/appspecific/com.chrome.devtools.json")
    public ResponseEntity<Void> ignoreDevToolsRequest() {
        return ResponseEntity.noContent().build();
    }
}