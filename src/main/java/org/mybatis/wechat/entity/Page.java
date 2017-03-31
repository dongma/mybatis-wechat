package org.mybatis.wechat.entity;

/**
 * Description: 实现分页的实体类
 *
 * @author: dong
 * @create: 2017-01-14-14:26
 */
public class Page {

    // 总条数
    private int totalNumber;

    // 当前的页数
    private int currentPage;

    // 总页数
    private int totalPage;

    // 每页显示的记录数.
    private int pageNumber = 5;

    // 数据库中的limit参数,从第几条开始取.
    private int dbIndex;

    // 数据库中的limit参数,每页取多少条的记录.
    private int dbNumber;

    /**
     * 根据当前对象中属性值计算并设置相关的属性值.
     * */
    public void count() {
        // 计算总页数
        int totalPageTemp = this.totalNumber / this.pageNumber;
        // 对总页数/每页的数量进行取余
        int plus = (this.totalNumber % this.pageNumber) == 0 ? 0 : 1;
        // 分页的总页数应该为 整除后的页数 + plus页数.
        totalPageTemp = totalPageTemp + plus;
        // 对totalPageTemp进行校验处理
        if(totalPageTemp <= 0) {
            totalPageTemp = 1;
        }
        this.totalPage = totalPageTemp;

        // 设置当前页数,总页数小于当前页数,应该将当前页数设置为总页数
        if(this.totalPage < this.currentPage) {
            this.currentPage = this.totalPage;
        }
        // 当前页数小于1设置为1
        if(this.currentPage < 1) {
            this.currentPage = 1;
        }

        // 设置limit参数
        this.dbIndex = (this.currentPage - 1) * this.pageNumber;
        this.dbNumber = this.pageNumber;
    }

    // setters and getters.
    public int getTotalNumber() {
        return totalNumber;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public int getDbNumber() {
        return dbNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
        count();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        count();
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    public void setDbNumber(int dbNumber) {
        this.dbNumber = dbNumber;
    }
}
