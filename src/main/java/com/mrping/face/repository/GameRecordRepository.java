package com.mrping.face.repository;

import com.mrping.face.entity.GameRecord;
import com.mrping.face.entity.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Created by Mr.Ping on 2018/7/13.
 * @version 1.0
 */
@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord,Long> {

    /**
     * 入库
     * @param gameRecord
     * @return
     */
    @Override
    GameRecord save(GameRecord gameRecord);

    /**
     * 查询游戏类型总数量
     * @param gameType
     * @return
     */
    @Query("select count(g) from GameRecord g where g.gameType = :gameType")
    Long getCountByGameType(@Param("gameType") GameType gameType);

    /**
     * 根据Id查询记录
     * @param userId
     * @param gameId
     * @return
     */
    @Query("select g from GameRecord g where g.userId = :userId and g.id = :gameId")
    GameRecord getGameRecordById(@Param("userId") Long userId, @Param("gameId") Long gameId);
}
