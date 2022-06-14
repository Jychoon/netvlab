package com.netvlab.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Userinfo {
    private int id;
    private String studentId;
    private String name;
    private String sex;
    private String school;
    private String classes;
    private String password;

    public  Userinfo(){}

    public  Userinfo(String studentId,String name,String sex,String school,String classes,String password){
        this.studentId=studentId;
        this.name=name;
        this.sex=sex;
        this.school=school;
        this.classes=classes;
        this.password=password;
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "studentId")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "school")
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Basic
    @Column(name = "classes")
    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Userinfo userinfo = (Userinfo) o;

        if (id != userinfo.id) return false;
        if (studentId != null ? !studentId.equals(userinfo.studentId) : userinfo.studentId != null) return false;
        if (name != null ? !name.equals(userinfo.name) : userinfo.name != null) return false;
        if (sex != null ? !sex.equals(userinfo.sex) : userinfo.sex != null) return false;
        if (school != null ? !school.equals(userinfo.school) : userinfo.school != null) return false;
        if (classes != null ? !classes.equals(userinfo.classes) : userinfo.classes != null) return false;
        if (password != null ? !password.equals(userinfo.password) : userinfo.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (school != null ? school.hashCode() : 0);
        result = 31 * result + (classes != null ? classes.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
