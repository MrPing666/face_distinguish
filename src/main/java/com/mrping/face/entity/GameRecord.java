package com.mrping.face.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Created by Mr.Ping on 2018/7/13.
 */
@Entity
@Table(name = "game_record")
public class GameRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**创建时间**/
    private Date crateTime = new Date();
    /**用户ID**/
    private Long userId;
    /**测试得分**/
    private Integer score;
    /**图片地址**/
    private String imageUrl;
    /**分享图片地址**/
    private String shareImgUrl;
    /**游戏类型**/
    private GameType gameType;

    public GameRecord() {}

    public GameRecord(Long userId, Integer score, GameType gameType) {
        this.userId = userId;
        this.score = score;
        this.gameType = gameType;
    }

    public GameRecord(Long userId, Integer score, String imageUrl, String shareImgUrl, GameType gameType) {
        this.userId = userId;
        this.score = score;
        this.imageUrl = imageUrl;
        this.shareImgUrl = shareImgUrl;
        this.gameType = gameType;
    }

    public void saveImageUrl(String imgUrl, String shareImgUrl){
        this.imageUrl = imgUrl;
        this.shareImgUrl = shareImgUrl;
    }

    public Date getCrateTime() {
        return crateTime;
    }

    public long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getScore() {
        return score;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getShareImgUrl() {
        return shareImgUrl;
    }

    public GameType getGameType() {
        return gameType;
    }
}
