package com.tenpo.app.service;

import com.tenpo.app.model.TenpSum;
import com.tenpo.app.repository.TenpoSumRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TenpoSumConsumer {
    @Autowired
    private TenpoSumRepository tenpoSumRepository;

    @KafkaListener(topics = "tenpSum_topic", groupId = "tenpSum_group")
    public void consume(TenpSum tenpSum) {
        tenpoSumRepository.save(tenpSum).subscribe();
    }
}
