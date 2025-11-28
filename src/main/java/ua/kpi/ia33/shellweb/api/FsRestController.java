package ua.kpi.ia33.shellweb.api;

import org.springframework.web.bind.annotation.*;
import ua.kpi.ia33.shellweb.service.FileOpsService;
import ua.kpi.ia33.shellweb.service.TokenService;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/fs")
public class FsRestController {

    private final FileOpsService fileOpsService;
    private final TokenService tokenService;

    public FsRestController(FileOpsService fileOpsService,
                            TokenService tokenService) {
        this.fileOpsService = fileOpsService;
        this.tokenService = tokenService;
    }

    @PostMapping("/copy")
    public void copy(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody FsCopyMoveRequest request
    ) throws Exception {
        tokenService.requireUser(token);
        fileOpsService.copy(
                Path.of(request.getSrc()),
                Path.of(request.getDst()),
                p -> {}
        );
    }

    @PostMapping("/move")
    public void move(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody FsCopyMoveRequest request
    ) throws Exception {
        tokenService.requireUser(token);
        fileOpsService.move(
                Path.of(request.getSrc()),
                Path.of(request.getDst()),
                p -> {}
        );
    }

    @PostMapping("/delete")
    public void delete(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody FsPathRequest request
    ) throws Exception {
        tokenService.requireUser(token);
        fileOpsService.delete(
                Path.of(request.getPath()),
                p -> {}
        );
    }

    @PostMapping("/rename")
    public void rename(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody FsCopyMoveRequest request
    ) throws Exception {
        tokenService.requireUser(token);
        fileOpsService.move(
                Path.of(request.getSrc()),
                Path.of(request.getDst()),
                p -> {}
        );
    }

    @PostMapping("/mkdir")
    public void mkdir(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody FsPathRequest request
    ) throws Exception {
        tokenService.requireUser(token);
        java.nio.file.Files.createDirectories(Path.of(request.getPath()));
    }
}
