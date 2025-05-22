package ureca.ureca_miniproject2.commoncode;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common-code")
@RequiredArgsConstructor
public class CodeController {

    private final CodeRepository codeRepository;

    @GetMapping
    public List<Code> getCodeNameList(@RequestParam("groupCode") String groupCode) {
        return codeRepository.findByGroupCode(groupCode);
    }
}
