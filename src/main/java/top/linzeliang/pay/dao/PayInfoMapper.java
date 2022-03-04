package top.linzeliang.pay.dao;

import org.apache.ibatis.annotations.Param;
import top.linzeliang.pay.pojo.PayInfo;

import java.util.List;

public interface PayInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);

    List<PayInfo> selectByOrderNoAndStatus(@Param("orderNo") Long orderNo, @Param("status") String status);

    int batchUpdate(@Param("payInfoList") List<PayInfo> payInfoList);

    PayInfo selectByMapOrderNo(@Param("mapOrderNo") Long mapOrderNo);

}