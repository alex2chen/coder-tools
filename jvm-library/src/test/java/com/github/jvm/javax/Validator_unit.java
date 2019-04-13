package com.github.jvm.javax;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.validation.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.executable.ExecutableValidator;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author alex.chen
 * @Description
 * @date 2016/9/24
 */
public class Validator_unit {
    private static Validator validator;

    @Before
    public void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void car_Test() {
        //test NotNull
        Car car = new Car(null, "red", 2);
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        Assert.assertEquals(1, violations.size());
        Assert.assertEquals("may not be null", violations.iterator().next().getMessage());
        //Size、Min
        car = new Car("Morris", "D", 4);
        violations = validator.validate(car);
        Assert.assertEquals(1, violations.size());
        Assert.assertEquals("size must be between 2 and 14", violations.iterator().next().getMessage());
        car = new Car("Morris", "DD-AB-123", 1);
        violations = validator.validateProperty(car, "brandName");
        Assert.assertEquals(1, violations.size());
        violations = validator.validateValue(Car.class, "brandName", null);
        Assert.assertEquals(1, violations.size());
    }

    @Test
    public void cars_Test() {
        Cars cars = new Cars();
        Set<ConstraintViolation<Cars>> violations = validator.validate(cars);
        Assert.assertEquals(0, violations.size());
        System.out.println(violations.size());
        List<Car> list = new ArrayList<>();
        list.add(new Car());
        cars.setCarlist(list);
        violations = validator.validate(cars);
        System.out.println(violations.size());
    }

    @Test
    public void person_test() throws NoSuchMethodException {
        //AssertTrue
        Person person = new Person(true, "15623789456");
        Set<ConstraintViolation<Person>> validators = validator.validate(person);
        System.out.println(validators.size());
        //method test
        Method method = Person.class.getMethod("sayHi", String.class);
        ExecutableValidator executableValidator = validator.forExecutables();
        Object[] parametreValues = {null};
        validators = executableValidator.validateParameters(person, method, parametreValues);
        System.out.println(validators.size());

        String returnValue = "";
        method = Person.class.getMethod("toMe", String.class, Integer.class);
        Integer durationInDays = 21;
        Object[] parameterValues = {"alex", durationInDays};
        executableValidator = validator.forExecutables();
        validators = executableValidator.validateReturnValue(person, method, returnValue, (Class<?>[]) parameterValues);
        System.out.println(validators.size());
        System.out.println(returnValue);
    }

    @Setter
    @Getter
    public static class Car {
        @NotNull
        private String brandName;

        @NotNull
        @Size(min = 2, max = 4)
        private String color;
        @Min(2)
        private int price;

        public Car() {
        }

        public Car(String brandName, String color, int price) {
            this.brandName = brandName;
            this.color = color;
            this.price = price;
        }
    }

    @Setter
    @Getter
    public static class Cars {
        @Valid
        private Car car;
        @NotNull
        @Size(min = 1)
        @Valid
        private List<Car> carlist;

        public Car getCar() {
            return car;
        }

        public void setCar(Car car) {
            this.car = car;
        }

        public List<Car> getCarlist() {
            return carlist;
        }

        public void setCarlist(List<Car> carlist) {
            this.carlist = carlist;
        }
    }

    @Setter
    @Getter
    public static class Person {
        @AssertTrue
        private boolean isEnabled;
        @Mobile
        private String mobile;

        public Person() {
        }

        public Person(boolean isEnabled, String mobile) {
            this.isEnabled = isEnabled;
            this.mobile = mobile;
        }
    }

    /**
     * 自定义注解实现前后台参数校验，判断是否包含非法字符
     * Created by YT on 2016/11/18.
     */
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Mobile.MobileValidator.class)
    public @interface Mobile {
        String message() default "The string is invalid.";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        class MobileValidator extends AbstractCustomValidator<Mobile, String> {
            private static final Pattern MOBILEPAR = Pattern.compile("^1\\d{10}$");

            @Override
            public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
                return Strings.isNullOrEmpty(s) || MOBILEPAR.matcher(s).matches();
            }
        }
    }

    public static abstract class AbstractCustomValidator<A extends Annotation, V extends Object> implements ConstraintValidator<A, V> {
        @Override
        public void initialize(A a) {
            //do nothing
        }
    }
}
