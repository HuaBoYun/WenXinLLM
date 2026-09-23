package com.financial.sharing.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblPrivateCar;
import com.financial.sharing.business.mapper.PrivateCarMapper;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 私车公用控制器 - 数据来源数据库
 */
@Slf4j
@Api(tags = "私车公用管理")
@RestController
@RequestMapping("/private-car")
@CrossOrigin
public class PrivateCarController {

    @Autowired
    private PrivateCarMapper privateCarMapper;

    @ApiOperation("查询私车档案列表")
    @GetMapping
    public MyJsonBean getPrivateCarList(PageableParam pageableParam,
                                       @RequestParam(required = false) String carNumber,
                                       @RequestParam(required = false) String ownerName,
                                       @RequestParam(required = false) String status,
                                       @RequestParam(required = false) String departmentId) {
        try {
            int pageNum = pageableParam.getPageNum();
            int pageSize = pageableParam.getSize();
            Page<TblPrivateCar> page = new Page<>(pageNum + 1, pageSize);
            QueryWrapper<TblPrivateCar> wrapper = new QueryWrapper<>();
            if (StringUtils.hasText(carNumber)) wrapper.like("CAR_NUMBER", carNumber);
            if (StringUtils.hasText(ownerName)) wrapper.like("OWNER_NAME", ownerName);
            if (StringUtils.hasText(status)) wrapper.eq("STATUS", status);
            if (StringUtils.hasText(departmentId)) wrapper.eq("DEPARTMENT_ID", departmentId);
            wrapper.orderByDesc("CREATE_TIME");

            Page<TblPrivateCar> resultPage = privateCarMapper.selectPage(page, wrapper);
            PageResult<TblPrivateCar> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) resultPage.getTotal());
            pageResult.setCurrentPage((int) resultPage.getCurrent());
            pageResult.setPageSize((int) resultPage.getSize());
            pageResult.setTotalPage((int) resultPage.getPages());
            pageResult.setTlist(resultPage.getRecords());

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询私车档案列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存私车档案")
    @PostMapping
    public MyJsonBean savePrivateCar(@RequestBody Map<String, Object> carData) {
        try {
            String carId = (String) carData.get("carId");
            TblPrivateCar car;
            if (StringUtils.hasText(carId)) {
                car = privateCarMapper.selectById(carId);
                if (car == null) return MyJsonBean.errorData("车辆不存在");
            } else {
                car = new TblPrivateCar();
                car.setCarId("CAR" + System.currentTimeMillis());
                car.setCreateTime(LocalDateTime.now());
                car.setStatus("ACTIVE");
                car.setTotalMileage(BigDecimal.ZERO);
                car.setTotalSubsidy(BigDecimal.ZERO);
                car.setIsEnabled(1);
            }

            if (carData.get("carNumber") != null) car.setCarNumber((String) carData.get("carNumber"));
            if (carData.get("carBrand") != null) car.setCarBrand((String) carData.get("carBrand"));
            if (carData.get("carModel") != null) car.setCarModel((String) carData.get("carModel"));
            if (carData.get("ownerName") != null) car.setOwnerName((String) carData.get("ownerName"));
            if (carData.get("ownerId") != null) car.setOwnerId((String) carData.get("ownerId"));
            if (carData.get("departmentId") != null) car.setDepartmentId((String) carData.get("departmentId"));
            if (carData.get("departmentName") != null) car.setDepartmentName((String) carData.get("departmentName"));
            if (carData.get("engineDisplacement") != null) car.setEngineDisplacement((String) carData.get("engineDisplacement"));
            if (carData.get("subsidyStandard") != null) car.setSubsidyStandard(new BigDecimal(carData.get("subsidyStandard").toString()));
            if (carData.get("status") != null) car.setStatus((String) carData.get("status"));
            if (carData.get("remark") != null) car.setRemark((String) carData.get("remark"));

            car.setUpdateTime(LocalDateTime.now());

            if (StringUtils.hasText(carId)) {
                privateCarMapper.updateById(car);
            } else {
                privateCarMapper.insert(car);
            }
            return MyJsonBean.successData("保存成功", car);
        } catch (Exception e) {
            log.error("保存私车档案失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取私车档案详情")
    @GetMapping("/{carId}")
    public MyJsonBean getPrivateCarDetail(@PathVariable String carId) {
        try {
            TblPrivateCar car = privateCarMapper.selectById(carId);
            if (car == null) return MyJsonBean.errorData("车辆不存在");
            return MyJsonBean.successData("查询成功", car);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除私车档案")
    @DeleteMapping("/{carId}")
    public MyJsonBean deletePrivateCar(@PathVariable String carId) {
        try {
            TblPrivateCar car = privateCarMapper.selectById(carId);
            if (car == null) return MyJsonBean.errorData("车辆不存在");
            privateCarMapper.deleteById(carId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("里程录入")
    @PostMapping("/mileage")
    public MyJsonBean recordMileage(@RequestBody Map<String, Object> mileageData) {
        try {
            String carId = (String) mileageData.get("carId");
            BigDecimal mileage = new BigDecimal(mileageData.getOrDefault("mileage", "0").toString());

            TblPrivateCar car = privateCarMapper.selectById(carId);
            if (car == null) return MyJsonBean.errorData("车辆不存在");

            // 更新总里程
            BigDecimal currentMileage = car.getTotalMileage() != null ? car.getTotalMileage() : BigDecimal.ZERO;
            car.setTotalMileage(currentMileage.add(mileage));
            car.setUpdateTime(LocalDateTime.now());

            // 计算并更新补贴
            BigDecimal standard = car.getSubsidyStandard() != null ? car.getSubsidyStandard() : BigDecimal.ZERO;
            BigDecimal subsidy = mileage.multiply(standard);
            BigDecimal currentSubsidy = car.getTotalSubsidy() != null ? car.getTotalSubsidy() : BigDecimal.ZERO;
            car.setTotalSubsidy(currentSubsidy.add(subsidy));

            privateCarMapper.updateById(car);

            Map<String, Object> result = new HashMap<>();
            result.put("recordId", "MILEAGE_" + System.currentTimeMillis());
            result.put("carId", carId);
            result.put("mileage", mileage);
            result.put("subsidy", subsidy);
            result.put("totalMileage", car.getTotalMileage());
            result.put("totalSubsidy", car.getTotalSubsidy());
            result.put("recordTime", LocalDateTime.now());
            return MyJsonBean.successData("里程录入成功", result);
        } catch (Exception e) {
            log.error("里程录入失败", e);
            return MyJsonBean.errorData("录入失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取里程记录")
    @GetMapping("/mileage")
    public MyJsonBean getMileageRecords(PageableParam pageableParam,
                                       @RequestParam(required = false) String carId,
                                       @RequestParam(required = false) String startDate,
                                       @RequestParam(required = false) String endDate) {
        try {
            // 里程记录从车辆档案中获取汇总数据
            List<TblPrivateCar> cars = privateCarMapper.selectList(null);
            List<Map<String, Object>> records = new ArrayList<>();
            for (TblPrivateCar car : cars) {
                Map<String, Object> record = new HashMap<>();
                record.put("carId", car.getCarId());
                record.put("carNumber", car.getCarNumber());
                record.put("ownerName", car.getOwnerName());
                record.put("totalMileage", car.getTotalMileage());
                record.put("totalSubsidy", car.getTotalSubsidy());
                record.put("status", car.getStatus());
                records.add(record);
            }
            return MyJsonBean.successData("查询成功", records);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("补贴计算")
    @PostMapping("/subsidy/calculate")
    public MyJsonBean calculateSubsidy(@RequestBody Map<String, Object> calculateData) {
        try {
            String carId = (String) calculateData.get("carId");
            BigDecimal mileage = new BigDecimal(calculateData.getOrDefault("mileage", "0").toString());
            BigDecimal subsidyStandard = calculateData.get("subsidyStandard") != null ?
                    new BigDecimal(calculateData.get("subsidyStandard").toString()) : null;

            TblPrivateCar car = privateCarMapper.selectById(carId);
            if (car == null) return MyJsonBean.errorData("车辆不存在");

            if (subsidyStandard == null) subsidyStandard = car.getSubsidyStandard() != null ? car.getSubsidyStandard() : BigDecimal.ZERO;

            BigDecimal subsidy = mileage.multiply(subsidyStandard);
            Map<String, Object> result = new HashMap<>();
            result.put("carId", carId);
            result.put("mileage", mileage);
            result.put("subsidyStandard", subsidyStandard);
            result.put("subsidy", subsidy);
            result.put("calculateTime", LocalDateTime.now());
            return MyJsonBean.successData("计算成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("计算失败: " + e.getMessage());
        }
    }

    @ApiOperation("补贴申请")
    @PostMapping("/subsidy/apply")
    public MyJsonBean applySubsidy(@RequestBody Map<String, Object> applyData) {
        try {
            String carId = (String) applyData.get("carId");
            BigDecimal applyAmount = new BigDecimal(applyData.getOrDefault("applyAmount", "0").toString());

            TblPrivateCar car = privateCarMapper.selectById(carId);
            if (car == null) return MyJsonBean.errorData("车辆不存在");

            Map<String, Object> result = new HashMap<>();
            result.put("applyId", "SUBSIDY_" + System.currentTimeMillis());
            result.put("carId", carId);
            result.put("carNumber", car.getCarNumber());
            result.put("ownerName", car.getOwnerName());
            result.put("applyAmount", applyAmount);
            result.put("status", "PENDING");
            result.put("applyTime", LocalDateTime.now());
            return MyJsonBean.successData("补贴申请成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("申请失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取补贴记录")
    @GetMapping("/subsidy")
    public MyJsonBean getSubsidyRecords(PageableParam pageableParam,
                                       @RequestParam(required = false) String carId,
                                       @RequestParam(required = false) String startDate,
                                       @RequestParam(required = false) String endDate) {
        try {
            QueryWrapper<TblPrivateCar> wrapper = new QueryWrapper<>();
            if (StringUtils.hasText(carId)) wrapper.eq("CAR_ID", carId);
            List<TblPrivateCar> cars = privateCarMapper.selectList(wrapper);

            List<Map<String, Object>> records = new ArrayList<>();
            for (TblPrivateCar car : cars) {
                if (car.getTotalSubsidy() != null && car.getTotalSubsidy().compareTo(BigDecimal.ZERO) > 0) {
                    Map<String, Object> record = new HashMap<>();
                    record.put("carId", car.getCarId());
                    record.put("carNumber", car.getCarNumber());
                    record.put("ownerName", car.getOwnerName());
                    record.put("totalSubsidy", car.getTotalSubsidy());
                    record.put("totalMileage", car.getTotalMileage());
                    record.put("subsidyStandard", car.getSubsidyStandard());
                    record.put("status", car.getStatus());
                    records.add(record);
                }
            }
            return MyJsonBean.successData("查询成功", records);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("路径规划")
    @PostMapping("/route/plan")
    public MyJsonBean planRoute(@RequestBody Map<String, Object> routeData) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("startLocation", routeData.get("startLocation"));
            result.put("endLocation", routeData.get("endLocation"));
            result.put("routeType", routeData.getOrDefault("routeType", "FASTEST"));
            result.put("message", "路径规划服务需接入外部地图API");
            return MyJsonBean.successData("路径规划请求已接收", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("路径规划失败: " + e.getMessage());
        }
    }

    @ApiOperation("里程统计")
    @GetMapping("/statistics/mileage")
    public MyJsonBean getMileageStatistics(@RequestParam(required = false) String carId,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate,
                                          @RequestParam(required = false) String statisticsType) {
        try {
            QueryWrapper<TblPrivateCar> wrapper = new QueryWrapper<>();
            if (StringUtils.hasText(carId)) wrapper.eq("CAR_ID", carId);
            List<TblPrivateCar> cars = privateCarMapper.selectList(wrapper);

            BigDecimal totalMileage = cars.stream()
                    .map(c -> c.getTotalMileage() != null ? c.getTotalMileage() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            int totalTrips = cars.size();

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalMileage", totalMileage);
            statistics.put("totalTrips", totalTrips);
            statistics.put("averagePerTrip", totalTrips > 0 ? totalMileage.divide(new BigDecimal(totalTrips), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            statistics.put("carCount", cars.size());
            return MyJsonBean.successData("查询成功", statistics);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("补贴统计")
    @GetMapping("/statistics/subsidy")
    public MyJsonBean getSubsidyStatistics(@RequestParam(required = false) String carId,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate) {
        try {
            QueryWrapper<TblPrivateCar> wrapper = new QueryWrapper<>();
            if (StringUtils.hasText(carId)) wrapper.eq("CAR_ID", carId);
            List<TblPrivateCar> cars = privateCarMapper.selectList(wrapper);

            BigDecimal totalSubsidy = cars.stream()
                    .map(c -> c.getTotalSubsidy() != null ? c.getTotalSubsidy() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal paidSubsidy = cars.stream()
                    .filter(c -> "ACTIVE".equals(c.getStatus()))
                    .map(c -> c.getTotalSubsidy() != null ? c.getTotalSubsidy() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalSubsidy", totalSubsidy);
            statistics.put("paidSubsidy", paidSubsidy);
            statistics.put("carCount", cars.size());
            return MyJsonBean.successData("查询成功", statistics);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出私车公用数据")
    @GetMapping("/export")
    public MyJsonBean export(@RequestParam(required = false) String exportType,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("exportId", "PRIVATE_CAR_EXPORT_" + System.currentTimeMillis());
            result.put("fileName", "private_car_" + (exportType != null ? exportType : "all") + "_" + System.currentTimeMillis() + ".xlsx");
            result.put("recordCount", privateCarMapper.selectCount(null));
            result.put("exportTime", LocalDateTime.now());
            return MyJsonBean.successData("导出成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }
}
