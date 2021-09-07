package com.bank.common.vo;

import com.bank.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口返回数据格式
 *
 */
@Data
@ApiModel(value = "接口返回对象", description = "接口返回对象")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    @ApiModelProperty(value = "成功标志")
    private boolean success = true;

    /**
     * 返回处理消息
     */
    @ApiModelProperty(value = "返回处理消息")
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    @ApiModelProperty(value = "返回代码", example = "0")
    private Integer code = 0;

    /**
     * 返回数据对象 data
     */
    @ApiModelProperty(value = "返回数据对象")
    private T result;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();

    public Result() {

    }

    public static <T> Result<T> ok() {
        Result r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage("成功");
        return r;
    }

    public static <T> Result<T> ok(String msg) {
        Result r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage(msg);
        return r;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> ok(Object data, Class<T> dest) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setResult(convertVO(data, dest));
        return r;
    }

    public static <T> Result<List<T>> ok(List<?> list, Class<T> dest) {
        Result<List<T>> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        List<T> t = new ArrayList<>();
        for (Object data : list) {
            t.add(convertVO(data, dest));
        }
        r.setResult(t);
        return r;
    }

    public static <T> Result<IPage<T>> ok(IPage page, Class<T> dest) {
        Result<IPage<T>> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        List<T> t = new ArrayList<>();
        for (Object data : page.getRecords()) {
            t.add(convertVO(data, dest));
        }
        page.setRecords(t);
        r.setResult(page);
        return r;
    }

    /**
     * 将对象转换成VO对象
     *
     * @param orig 源数据
     * @param dest 目标类型
     * @return
     */
    private static <T> T convertVO(Object orig, Class<T> dest) {
        if (orig == null) {
            return null;
        }
        try {
            T t = dest.newInstance();
            BeanUtils.copyProperties(orig, t);
            return t;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException();
        }
    }

    public static <T> Result<T> error(String msg) {
        return error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, msg);
    }

    public static <T> Result<T> error(int code, String msg) {
        Result r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    /**
     * 无权限访问返回结果
     */
    public static Result noAuth(String msg) {
        return error(CommonConstant.NO_AUTHZ, msg);
    }
}
