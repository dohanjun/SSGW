package com.yedam.app.group.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yedam.app.group.mapper.BasketMapper;

@Component
public class BasketCleanupScheduler {
	private final BasketMapper basketMapper;

    public BasketCleanupScheduler(BasketMapper basketMapper) {
        this.basketMapper = basketMapper;
    }

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteOldBasketData() {
        List<Long> expiredWritingIds = basketMapper.selectExpiredBasketWritingIds();

        for (Long id : expiredWritingIds) {
            basketMapper.deleteFilesByWritingId(id);
            basketMapper.deletePostByWritingId(id);
            basketMapper.deleteBasketByWritingId(id);
        }

    }
}
