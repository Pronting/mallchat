package priv.pront.mallchat.common.common.exception;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Charsets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import priv.pront.mallchat.common.common.domain.vo.resp.ApiResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@Getter
public enum HttpErrorEnum implements ErrorEnum{

    ACCESS_DENIED(401, "权限错误");

    Integer code;
    String msg;

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(this.code);
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        ApiResult<Object> fail = ApiResult.fail(this);
        response.getWriter().write(JSONUtil.toJsonStr(fail));
    }

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
