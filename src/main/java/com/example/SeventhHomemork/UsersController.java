package com.example.SeventhHomemork;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
public class UsersController {

    @GetMapping("/users")
    //原因：クライアントが要求したURL文字列に処理に支障をきたすような内容が含まれ、これを処理するサーバ側のプログラムが異常終了してしまった
    //ステータスコード：500
    public String users(@RequestParam("name") @NotBlank(message = "名前を入力してください") String name,
                        @RequestParam("age") @Range(max = 150, min = 0, message = "入力範囲超えています") Integer age,
                        @RequestParam("birthday") @DateTimeFormat(pattern = "yyyy/MM/dd") String birthday) {
        return "入力結果は：" + "名前" + name + "　年齢" + age + "　生年月日" + birthday + "です。";
    }

    //これより下はBadRequestでのエラーとして表示
    //ステータスコードは400
    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> create(@RequestBody @Validated CreateForm users) {
        // 登録処理は省略
        URI url = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/users/id") // id部分は実際に登録された際に発⾏したidを設定する
                .build()
                .toUri();
        //MapにすることでJSON形式で返してくれるようになる、データのやりとりがスムーズ
        return ResponseEntity.created(url).body(Map.of("message", "name successfully created"));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable("id") int id, @RequestBody @Validated UpdateForm form) {
        // 更新処理は省略
        return ResponseEntity.ok(Map.of("message", "name successfully updated"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") int id) {
        return ResponseEntity.ok(Map.of("message", "name successfully deleted"));
    }

    @ExceptionHandler(Exception.class)
    public Map<String, String> Exceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return errors;
    }
}