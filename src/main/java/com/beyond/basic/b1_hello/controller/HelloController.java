package com.beyond.basic.b1_hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


//Component 어노테이션(Controller 안에 내장된 기능?) 을 통해 별도의 객체를 생성할 필요가 없는 싱글톤 객체 생성
//Controller 어노테이션을 통해 쉽게 사용자의 http req를 분석하고, http res를 생성

// 사용자의 요청에 대한 패턴을 외우는게 중요(1. path방식 2. param방식 )
// 파라미터 형식의 데이터 바인딩(1. get 2. post)
// 1. get : header부 ?id=1 (조회)
// 2. post : body부 url인코딩,form(?asdf = 12 & ... 이므로 json은 놉)
// 둘 다 처리는 똑가트니 requestparam(한개) -> modelattribute(여러개 : 데이터 바인딩)

// 또한 1. json(requestbody) 혹은 2. formdata(modelatribute) 처리방식을 외워야 한다.

//--------------------------------------------------

// path방식(@pathvariable) , json 방식(@requestbody)은 다른 case


@Controller

//클래스 차원의 url매핑시에는 RequestMapping을 사용
@RequestMapping("/hello")
public class HelloController {


//    get요청의 case들
//    case1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody 있을 때

    @GetMapping("/hello") //아래 메서드에 대한 서버의 엔드포인트를 설정 : if get요청이면~
    @ResponseBody //Responsebody가 없고 return타입이 string일 경우 서버는 templates폴더 밑에 helloworld.html을 찾아서 리턴
    public String helloWorld() {
        return "helloworld";
    }

//    case2. 서버가 사용자에게 String(json형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Hello helloJson() throws JsonProcessingException {

        Hello h1 = new Hello("hong","hong@naver.com");
//        직접 json으로 직렬화 할 필요 없이, return타입에 객체가 있으면 자동으로 직렬화
//        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(h1);
        return h1;
    }

//    case3. parameter 방식을 통해 사용자로부터 값을 수신
//    parameter의 형식 : /member?name=hongildong
    @GetMapping("/param")
    @ResponseBody
    public Hello param(@RequestParam(value = "name")String inputName) {
        Hello h2 = new Hello(inputName,"hong2@naver.com");

        return h2;
    }

//    case4. pathvariable 방식을 통해 사용자로부터 값을 수신
//    pathvariable의 형식 : /member/1
//    pathvariable방식은 url을 통해 자원의 구조를 명확하게 표현할 때 사용.(좀 더 restful)
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String Path(@PathVariable Long inputId) {
//        별도의 형변환 없어도 매개변수에 타입지정시 자동형변환 시켜줌
//        long id = Long.parseLong(inputId);
        System.out.println(inputId);
        return "ok";
    }

//    case5. parameter 2개 이상 형식
//    /param2?name=hong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody

    public String param2(@RequestParam(value = "name") String inputName, @RequestParam(value = "email") String inputEmail) {
        System.out.println(inputName);
        System.out.println(inputEmail);
        return "ok";
    }


//    case6. parameter가 많아질 경우 데이터 바인딩을 통해 input값 처리(빈번한 패턴, 중요!!)
//    데이터바인딩 : param을 이용하여 객체로 생성
//    ?name=hong&email=hong@naver.com

//    어차피 데이터 받아서 객체 만들것아니냐? model atri~로 명시해라 그냥 객체로 받고.
    @GetMapping("/param3")
    @ResponseBody
//    public String param3 (Hello hello)
    public String param3 (@ModelAttribute Hello hello){ //ModelAttribute를 써도 되고 안써도 되지만 param인 것을 명시
//
        System.out.println(hello);
        System.out.println(hello.getName());
        return "ok";
    }


//    case7. 서버에서 화면을 return, 사용자로부터 넘어오는 input값을 활용하여 동적인 화면 생성.
//    서버에서 화면+데이터를 렌더링해주는 ssr방식(csr방식은 서버는 데이터만)
//    mvc(model, view, controller)패턴이라고도 함
    @GetMapping("/model-Param")
//    Model 객체는 데이터를 화면에 전달해주는 역할을 한다.
//    name이라는 키에 hongildong 이라는 value를 key:value형태로 화면에 전달해준다.
    public String modelParam(@RequestParam(value = "id") Long inputId, Model model) {
        if(inputId==1) {
            model.addAttribute("name","hongildong1");
            model.addAttribute("email","hongildong1@naver.com");

        } else if(inputId==2) {
            model.addAttribute("name","hongildong2");
            model.addAttribute("email","hongildong2@naver.com");

        }
        return "helloworld2";
    }


//    post요청의 case들 : formdata(multipart 혹은 url인코딩) 또는 json

//    case1. text만 있는 formdata형식
//    형식 : body부에 name-xxx&email=xxx
    @GetMapping("/form-view")
    public String formView () {
        return "form-view";
    }
//    get요청의 url에 파라미터방식과 동일한 데이터 형식이므로 RequestParam 또는 데이터 바인딩 방식 가능하다.
    @PostMapping("/form-view")
    @ResponseBody
    public String formViewPost (@ModelAttribute Hello hello) {
        System.out.println(hello);
        return "ok";
    }

//    case2-1. text와 file이 있는 form-data형식(순수html로 제출)
@GetMapping("/form-file-view")
public String formFileView () {
    return "form-file-view";
}
    @PostMapping("/form-file-view")
    @ResponseBody
    public String formFileViewPost (@ModelAttribute Hello hello, @RequestParam(value = "photo")MultipartFile photo) {
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "ok";
    }

    // case 2-2. text와 file이 있는 form-data 형식 (js로 제출)
    @GetMapping("/axios-file-view")
    public String axiosFileView() {
        return "axios-file-view";
    }


//    case3. text와 멀티 file이 있는 form-data형식 (js로 제출)

    @GetMapping("/axios-multi-file-view")
    public String axiosMultiFileView () {
        return "axios-multi-file-view";
    }
    @PostMapping("/axios-multi-file-view")
    @ResponseBody
    public String axiosMultiFileView (@ModelAttribute Hello hello, @RequestParam(value = "photos") List<MultipartFile> photos) {
        for(int i=0; i<photos.size(); i++) {
            System.out.println(photos.get(i).getOriginalFilename());
        }
        return "ok";
    }


//    case4. json 데이터 처리
    @GetMapping("/axios-json-view")
    public String axiosJsonView () {
        return "axios_json_view";
    }

//    RequestBody : json형식으로 데이터가 들어올때 객체로 자동파싱(매우중요!!!!!!!!!!!!!!!!!!!!!!!!!!)
    @PostMapping("/axios-json-view")
    @ResponseBody
    public String axiosJsonViewPost(@RequestBody Hello hello) { //RequestBody로 바디의 데이터를 가져올 수 있음
        System.out.println(hello);
        return "okay";
    }

//    case5. 중첩된 json 데이터 처리(정말 너무 흔하고 너무 자주 나옴 중요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!)
@GetMapping("/axios-nested-json-view")
public String axiosNestedJsonView () {
    return "axios-nested-json-view";
}

    @PostMapping("/axios-nested-json-view")
    @ResponseBody
    public String axiosNestedJsonViewPost(@RequestBody Student student) {
        System.out.println(student);
        return "okay";
    }


//    case6. json(text)과 file을 같이 처리 : text구조가 복잡하여 피치못하게 json구조를 써야하는 경우
//    데이터형식 : hello={name:"xx",email:"xxx"}&photo=imagae.jpg
//    결론은 단순 json구조가 아닌 multipart-formdata 구조안에 json을 넣는 구조
@GetMapping("/axios-json-file-view")
public String axiosJsonFileView () {
    return "/axios-json-file-view";
}

    @PostMapping("/axios-json-file-view")
    @ResponseBody
//    json과 file을 함께 처리해야할 때 requestPart 일반적으로 활용
    public String axiosJsonFileViewPost(@RequestPart("hello") Hello hello,
                                        @RequestPart("photo") MultipartFile photo) {
        System.out.println();
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "okay";
    }




}
