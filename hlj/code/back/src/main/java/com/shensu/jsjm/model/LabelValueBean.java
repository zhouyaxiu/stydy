
package com.shensu.jsjm.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class LabelValueBean implements Comparable, Serializable {
    @ApiModelProperty("年龄组名称")
    private String                 label                  = null;

    @ApiModelProperty("年龄组值")
    private String                 value                  = null;

    public LabelValueBean() {
        super();
    }

    public LabelValueBean(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int compareTo(Object o) {
        String otherLabel = ((LabelValueBean) o).getLabel();

        return this.getLabel().compareTo(otherLabel);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("LabelValueBean[");

        sb.append(this.label);
        sb.append(", ");
        sb.append(this.value);
        sb.append("]");

        return (sb.toString());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof LabelValueBean)) {
            return false;
        }

        LabelValueBean bean = (LabelValueBean) obj;
        int nil = (this.getValue() == null) ? 1 : 0;

        nil += ((bean.getValue() == null) ? 1 : 0);

        if (nil == 2) {
            return true;
        } else if (nil == 1) {
            return false;
        } else {
            return this.getValue().equals(bean.getValue());
        }
    }

    public int hashCode() {
        return (this.getValue() == null) ? 17 : this.getValue().hashCode();
    }
}
