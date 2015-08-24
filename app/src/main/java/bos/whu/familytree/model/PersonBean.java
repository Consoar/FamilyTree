package bos.whu.familytree.model;


/**
 * Created by Consoar on 2015/3/27.
 */
public class PersonBean {
    private long gid = 0;
    private long pid = 0;
    private long ggen = 0;
    private String photourl = null;
    private String  pdetail = "";
    private String firstname = "";
    private String fullname = "";
    private String birthday = "";
    private String bornplace = "";
    private String borndsc = "";
    private int islive = 1;
    private String deathday = "";
    private String deathplace = "";
    private String graveyard = "";
    private String deathdsc = "";
    private int sex = 1; //0未知  1男 2女
    private long wifeID = 0;
    private long fatherID = 0;
    private long motherID = 0;
    private PersonBean wife = null;
    private PersonBean father = null;
    private PersonBean mother = null;

    public long getGid() {
        return gid;
    }
    public void setGid(long gid) {
        this.gid = gid;
    }
    public long getPid() {
        return pid;
    }
    public void setPid(long pid) {
        this.pid = pid;
    }
    public long getGgen() {
        return ggen;
    }
    public void setGgen(long ggen) {
        this.ggen = ggen;
    }
    public String getPhotourl() {
        return photourl;
    }
    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
    public String getPdetail() {
        return pdetail;
    }
    public void setPdetail(String pdetail) {
        this.pdetail = pdetail;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getBornplace() {
        return bornplace;
    }
    public void setBornplace(String bornplace) {
        this.bornplace = bornplace;
    }
    public String getBorndsc() {
        return borndsc;
    }
    public void setBorndsc(String borndsc) {
        this.borndsc = borndsc;
    }
    public int getIslive() {
        return islive;
    }
    public void setIslive(int islive) {
        this.islive = islive;
    }
    public String getDeathday() {
        return deathday;
    }
    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }
    public String getDeathplace() {
        return deathplace;
    }
    public void setDeathplace(String deathplace) {
        this.deathplace = deathplace;
    }
    public String getGraveyard() {
        return graveyard;
    }
    public void setGraveyard(String graveyard) {
        this.graveyard = graveyard;
    }
    public String getDeathdsc() {
        return deathdsc;
    }
    public void setDeathdsc(String deathdsc) {
        this.deathdsc = deathdsc;
    }
    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public long getWifeID() {
        return wifeID;
    }
    public void setWifeID(long wifeID) {
        this.wifeID = wifeID;
    }
    public long getFatherID() {
        return fatherID;
    }
    public void setFatherID(long fatherID) {
        this.fatherID = fatherID;
    }
    public long getMotherID() {
        return motherID;
    }
    public void setMotherID(long motherID) {
        this.motherID = motherID;
    }
    public PersonBean getWife() {
        return wife;
    }
    public void setWife(PersonBean wife) {
        this.wife = wife;
    }
    public PersonBean getFather() {
        return father;
    }
    public void setFather(PersonBean father) {
        this.father = father;
    }
    public PersonBean getMother() {
        return mother;
    }
    public void setMother(PersonBean mother) {
        this.mother = mother;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "gid=" + gid +
                ", pid=" + pid +
                ", ggen=" + ggen +
                ", photourl='" + photourl + '\'' +
                ", pdetail='" + pdetail + '\'' +
                ", firstname='" + firstname + '\'' +
                ", fullname='" + fullname + '\'' +
                ", birthday='" + birthday + '\'' +
                ", bornplace='" + bornplace + '\'' +
                ", borndsc='" + borndsc + '\'' +
                ", islive=" + islive +
                ", deathday='" + deathday + '\'' +
                ", deathplace='" + deathplace + '\'' +
                ", graveyard='" + graveyard + '\'' +
                ", deathdsc='" + deathdsc + '\'' +
                ", sex=" + sex +
                ", wifeID=" + wifeID +
                ", fatherID=" + fatherID +
                ", motherID=" + motherID +
                '}';
    }
}
