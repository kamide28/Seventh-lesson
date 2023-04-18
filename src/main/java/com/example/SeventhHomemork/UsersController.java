package com.example.SeventhHomemork;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class UsersController {

    @GetMapping("/users")
    public String users(@RequestParam("name") @NotBlank(message = "名前を入力してください") String name,
                        @RequestParam("age") @Range(max = 150, min = 0, message = "入力範囲超えています") Integer age,
                        @RequestParam("birthday") @DateTimeFormat(pattern = "yyyy/MM/dd") String birthday) {
        return "入力結果は：" + "名前" + name + "　年齢" + age + "　生年月日" + birthday + "です。";
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> create(@RequestBody @Validated CreateForm createForm) {
        // 登録処理は省略
        URI url = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/users/id") // id部分は実際に登録された際に発⾏したidを設定する
                .build()
                .toUri();
        return ResponseEntity.created(url).body(Map.of("message", "name successfully created"));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable("id") int id, @RequestBody @Validated UpdateForm updateForm) {
        // 更新処理は省略
        return ResponseEntity.ok(Map.of("message", "name successfully updated"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") int id) {
        return ResponseEntity.ok(Map.of("message", "name successfully deleted"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        Map<String, String> methodErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((methodError) -> {
            String fieldName = ((FieldError) methodError).getField();
            String methodErrorMassage = methodError.getDefaultMessage();
            methodErrors.put(fieldName, methodErrorMassage);
        });
        return methodErrors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public List<Object> handleConstraintViolationException(
            ConstraintViolationException ex) {
        List<Object> constraintErrors = new ArrayList<Object>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            constraintErrors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        return constraintErrors;
    }

}
