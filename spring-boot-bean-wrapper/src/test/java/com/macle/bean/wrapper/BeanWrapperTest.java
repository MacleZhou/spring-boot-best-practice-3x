package com.macle.bean.wrapper;

import com.macle.bean.wrapper.converts.StringToUserConvert;
import com.macle.bean.wrapper.editors.StringToUserEditor;
import com.macle.bean.wrapper.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;
import org.springframework.core.convert.support.DefaultConversionService;

@Slf4j
public class BeanWrapperTest {
    @Test
    public void testBeanWrapper(){
        BeanWrapper company = new BeanWrapperImpl(new Company()) ;
// 设置属性值
        company.setPropertyValue("name", "Pack Company Inc.");
// 也可以向下面这样
        PropertyValue value = new PropertyValue("name", "Pack Company Inc.");
        company.setPropertyValue(value);

// 创建员工并绑定到Company对象上
        Employee emp = new Employee() ;
        BeanWrapper empWrapper = new BeanWrapperImpl(emp) ;
        empWrapper.setPropertyValue("name", "张三") ;
        empWrapper.setPropertyValue("salary", 66666.66) ;
        company.setPropertyValue("employee", empWrapper.getWrappedInstance()) ;

// 获取薪水；嵌套属性访问
        Float salary = (Float) company.getPropertyValue("employee.salary") ;
        log.info("employee {}'s salary is {}", empWrapper.getPropertyValue("name"), salary);
        System.out.println(company.getWrappedInstance()) ;
    }

    @Test
    public void testBeanWrapperAccessViaIndex(){
        Employee emp = new Employee() ;
        BeanWrapper empWrapper = new BeanWrapperImpl(emp) ;
        empWrapper.setPropertyValue("name", "张三") ;
        empWrapper.setPropertyValue("address[0]", "新疆") ;
        empWrapper.setPropertyValue("address[1]", "重庆") ;
        System.out.println(empWrapper.getPropertyValue("address[1]"));
    }

    @Test
    public void testBeanWrapperAccessViaKeyValueMap(){
        BeanWrapper empWrapper = new BeanWrapperImpl(Employee.class) ;
        empWrapper.setPropertyValue("account[home]", "H0001") ;
        empWrapper.setPropertyValue("account[work]", "W0001") ;
        System.out.println(empWrapper.getPropertyValue("account[work]"));
    }

    @Test
    public void testBeanWrapperDefaultValueConvert(){
        BeanWrapper empWrapper = new BeanWrapperImpl(Employee.class) ;
        empWrapper.setPropertyValue("name", "Macle");
        empWrapper.setPropertyValue("salary", "888.88") ;
        log.info("employee {}'s salary is {}", empWrapper.getPropertyValue("name"), empWrapper.getPropertyValue("salary"));
    }

    @Test
    public void testBeanWrapperCustomizedEditor(){
        BeanWrapper wrapper = new BeanWrapperImpl(Pack.class) ;
        wrapper.registerCustomEditor(User.class, new StringToUserEditor()) ;
        wrapper.setPropertyValue("user", "23,张三") ;
        System.out.println(wrapper.getWrappedInstance()) ;
        Pack pack = (Pack)wrapper.getWrappedInstance();
        log.info("pack's {}'s age is {}", pack.getUser().getName(), pack.getUser().getAge());
    }

    @Test
    public void testBeanWrapperConvertService(){
        BeanWrapper wrapper = new BeanWrapperImpl(Pack.class) ;
        DefaultConversionService conversionService = new DefaultConversionService() ;
        conversionService.addConverter(new StringToUserConvert());
        wrapper.setConversionService(conversionService) ;
        wrapper.setPropertyValue("user", "23,张三") ;
        Pack pack = (Pack)wrapper.getWrappedInstance();
        log.info("pack's {}'s age is {}", pack.getUser().getName(), pack.getUser().getAge());
        log.info("" + wrapper.getWrappedInstance());
    }

    @Test
    public void testBeanWrapperSequenceOfConvertAndEditor(){
        BeanWrapper wrapper = new BeanWrapperImpl(Pack.class) ;

        DefaultConversionService conversionService = new DefaultConversionService() ;
        conversionService.addConverter(new StringToUserConvert());
        wrapper.setConversionService(conversionService) ;
        wrapper.registerCustomEditor(User.class, new StringToUserEditor()) ;//自定义的StringToUserEditor生效了，优先级高于conversionService
        wrapper.setPropertyValue("user", "23,张三") ;

        System.out.println(wrapper.getWrappedInstance()) ;

    }

    /***
     * 在使用PropertyEditor进行类型转换时，如果你要转换到的类型同包中有XxxEditor存在（Xxx，你要转换的类型），则你可以不用注册，
     * BeanWrapper内部会自动的在该包中进行查找，如这里要转换的User对应查找UserEditor类。
     * */
    @Test
    public void testBeanWrapperAutoApplyEditor(){
        BeanWrapper wrapper = new BeanWrapperImpl(Pack2.class) ;
        wrapper.setPropertyValue("user2", "23,张三") ;
        System.out.println(wrapper.getWrappedInstance()) ;
    }
}
