package com.beyond.board.post.service;

import com.beyond.board.post.domain.Post;
import com.beyond.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Component
@Slf4j
//스케쥴러 서버를 2대 이상 운영하게 되면 중복 스케줄이 돌아갈 여지가 있으므로,
//redis등의 솔루션을 사용하여 스케쥴 제어
public class PostScheduler {

    private final PostRepository postRepository;


    @Scheduled(cron = "0 0/1 * * * *")
    public void postSchedule() {
      log.info("========예약스케쥴러 시작==========");
        List<Post> posts = postRepository.findByAppointment("Y");
        LocalDateTime now = LocalDateTime.now();
        for(Post p : posts) {
            if(p.getAppointmentTime().isBefore(now)) {
                p.updateAppointment("N");
            }
        }
      log.info("========예약스케쥴러 끝==========");
    }

}
