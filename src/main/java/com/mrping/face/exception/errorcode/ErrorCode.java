package com.mrping.face.exception.errorcode;


/**
 * @author Created by Mr.Ping on 2018/7/9.
 */
public enum ErrorCode {

    SUCCESS("成功"),
    FAULURE("操作失败"),
    ERR1001("JSON格式错误"),
    ERR1002("参数错误"),
    ERR1003("账号已存在"),
    ERR1004("账号或密码错误"),
    ERR1005("TOKEN无效"),
    ERR1006("请求超时"),
    ERR1007("参数不能为空"),
    ERR1008("验签失败，请重新登录"),
    ERR1009("校验码错误"),
    ERR1010("短信发送失败"),
    ERR1011("账号不存在"),
    ERR1012("一天之类同一类型短信超过5次"),
    ERR1013("1分钟之内不能重复发送"),
    ERR1014("校验码已过期"),
    ERR1015("手机号码不能为空"),
    ERR1016("数字账号错误"),
    ERR1017("TOKEN已过期"),
    ERR1018("用户名不能为空"),
    ERR1019("密码不能为空"),
    ERR1020("用户已被停用"),
    ERR1021("错误次数过多"),
    ERR1022("校验码不能为空"),
    ERR1023("请更新最新版"),
    ERR1024("半年内无账单"),
    ERR1025("登录密码和支付密码不能相同"),
    ERR1026("登录密码与原登录密码相同"),
    ERR1027("用户名不正确"),
    ERR1028("数字账号不能为空"),
    ERR1029("银行卡绑定失败"),
    ERR1030("设置默认银行卡失败"),
    ERR1031("删除银行卡失败"),
    ERR1032("账户内转账失败"),
    ERR1033("转到银行卡失败"),
    ERR1034("提现失败"),
    ERR1035("密码错误"),
    ERR1036("未设置安全问题"),
    ERR1037("答案错误"),
    ERR1038("证件号错误"),
    ERR1039("支付密码和登录不能相同"),
    ERR1040("支付密码与原支付密码相同"),
    ERR1041("认证失败"),
    ERR1042("认证接口繁忙，请稍后再试"),
    ERR1043("查询次数已经用完"),
    ERR1044("该用户已通过认证"),
    ERR1045("支付密码错误"),
    ERR1046("指纹支付异常，请重新开通或使用密码支付"),
    ERR1047("您还未开通或已关闭指纹支付"),
    ERR1048("对方已经申请加你为好友"),
    ERR1049("实名认证信息有误"),
    ERR1050("实名信息已被认证"),
    ERR1051("请先进行实名认证"),

    ERR2500("远程服务器异常"),
    ERR2404("接口不存在"),
    ERR2501("登录异常"),

    ERR2502("您所在的网络被禁止操作，若有疑问，请与客服联系"),
    ERR2503("您所使用的设备被禁止操作，若有疑问，请与客服联系"),
    ERR2504("您所使用的设备信息异常，若有疑问，请与客服联系"),
    ;


    private final String text;

    public String getText() {
        return text;
    }

    public String getLabel() {
        return toString();
    }

    ErrorCode(String text) {
        this.text = text;
    }

}