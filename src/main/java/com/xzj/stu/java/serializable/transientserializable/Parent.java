package com.xzj.stu.java.serializable.transientserializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhijunxie
 * @date 2019/3/6
 */
@Setter
@Getter
public class Parent {
    private String attr1;

    private String attr2;

    private String attr3;

    public Parent() {

    }

    public Parent(String attr1, String attr2, String attr3) {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
    }
}
