package nyata.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ModelAndViewController {
    @PostMapping(value = "/")
    public ModelAndView post(@RequestParam("upload_file") MultipartFile uploadFile, ModelAndView mav) {
        mav.setViewName("index");
        mav.addObject("file_contents", fileContents(uploadFile));
        return mav;
    }

    private List<String> fileContents(MultipartFile uploadFile) {
        List<String> lines = new ArrayList<String>();
        String line = null;
        try {
            InputStream stream = uploadFile.getInputStream();
            Reader reader = new InputStreamReader(stream);
            BufferedReader buf = new BufferedReader(reader);
            while ((line = buf.readLine()) != null) {
                lines.add(line);
            }
            line = buf.readLine();
        } catch (Exception e) {
            line = "Can't read contents";
            lines.add(line);
            e.printStackTrace();
        }
        return lines;
    }
}