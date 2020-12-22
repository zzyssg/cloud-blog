package com.zzy.cloudblogblog.controller;

import com.zzy.cloudblogblog.entity.ResponseBean;
import com.zzy.cloudblogblog.entity.Type;
import com.zzy.cloudblogblog.enums.ResponseEnum;
import com.zzy.cloudblogblog.exception.CommonException;
import com.zzy.cloudblogblog.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/22 14:00
 */
@RestController("type")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TypeController {

    private final TypeService typeService;

    @GetMapping("/queryTypeByTypeId/{typeId}")
    public ResponseBean queryTypeById(@PathVariable Long typeId) {
        ResponseBean result;
        Type typeById = typeService.getTypeById(typeId);
        if (typeById == null) {
            log.error("此typeID下的类型为空!");
            throw new CommonException(ResponseEnum.TYPE_ONID_IS_NULL.getCode(),
                    ResponseEnum.TYPE_ONID_IS_NULL.getMsg());
        }
        log.info("id为{}的类型为{}", typeId, typeById);
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                typeById);
        return result;
    }

    @GetMapping("/queryAllTypes")
    public ResponseBean queryAllTypes() {
        ResponseBean result;
        List<Type> types = typeService.listAllTypes();
        //类型为空
        if (types == null || types.size() == 0) {
            log.error(ResponseEnum.TYPE_IS_NULL.getMsg());
            throw new CommonException(ResponseEnum.TYPE_IS_NULL.getCode(),
                    ResponseEnum.TYPE_IS_NULL.getMsg());
        }
        log.info("博客类型查询成功!");
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                types);

        return result;
    }

    @PostMapping("/addType")
    public ResponseBean addType(@RequestBody Type type) {
        ResponseBean result;
        typeService.insertType(type);
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                type);
        return result;
    }

    @GetMapping("/deleteTypeByTypeId")
    public ResponseBean deleteTypeById(@RequestParam("typeId") Long typeId) {
        ResponseBean result;
        //删除之前先查询是否存在此类型
        Type typeById = typeService.getTypeById(typeId);
        if (typeById == null) {
            log.error(ResponseEnum.TYPE_ONID_IS_NULL.getMsg());
            throw new CommonException(ResponseEnum.TYPE_ONID_IS_NULL.getCode(),
                    ResponseEnum.TYPE_ONID_IS_NULL.getMsg());
        }
        //存在此类型，删除
        Boolean deleteSuccess = typeService.deleteType(typeById);
        if (!deleteSuccess) {
            //删除失败
            result = new ResponseBean(ResponseEnum.RESPONSE_FAILED.getCode(), ResponseEnum.DELETE_TYPE_ERROR.getMsg(),
                    typeById);
        } else {
            //删除成功
            result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(), ResponseEnum.RESPONSE_SUCCESS.getMsg(), typeById);
        }
        return result;
    }


}
