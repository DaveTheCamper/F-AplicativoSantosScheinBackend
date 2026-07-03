package br.com.santosschein.complexo.infrastructure.persistence.mapper;

import br.com.santosschein.complexo.domain.model.ReservationSlot;
import br.com.santosschein.complexo.domain.model.SoccerField;
import br.com.santosschein.complexo.infrastructure.persistence.entity.ReservationSlotEntity;
import br.com.santosschein.complexo.infrastructure.persistence.entity.SoccerFieldEntity;

import java.time.LocalTime;

public class ReservationSlotPersistenceMapper {

    public ReservationSlot toDomain(ReservationSlotEntity entity) {
        return new ReservationSlot(
                entity.getId(),
                toDomain(entity.getField()),
                LocalTime.parse(entity.getStartTime()),
                LocalTime.parse(entity.getEndTime()),
                entity.getPrice(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private SoccerField toDomain(SoccerFieldEntity entity) {
        return new SoccerField(
                entity.getId(),
                entity.getName(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
