package cn.azhicloud.idgen.service.impl;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import cn.azhicloud.idgen.model.entity.IdGen;
import cn.azhicloud.idgen.repo.IdGenRepo;
import cn.azhicloud.idgen.service.IdGenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/30 17:49
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IdGenServiceImpl implements IdGenService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    private final IdGenRepo idGenRepo;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    public Long genNewId() {
        IdGen saved = idGenRepo.save(new IdGen());

        Long seqKey = getSeqKey(saved.getSeq());
        saved.setSeqKey(seqKey);

        return seqKey;
    }

    private String getTimestamp() {
        return ZonedDateTime.now(ZONE_ID).format(FORMATTER);
    }

    private String getSeqString(Long seq) {
        return new DecimalFormat("0000").format(seq % 10000);
    }

    private Long getSeqKey(Long seq) {
        return Long.valueOf(getTimestamp() + getSeqString(seq));
    }
}
