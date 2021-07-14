package com.qkl.online.mining.app.mvp.view;

import com.qkl.online.mining.app.data.entity.BaseBean;

/**
 * @param <T>
 */

public interface IUserInfoView<T extends BaseBean> extends IBaseView {

    void showLoading();

    void hideLoading();

    void logOut();

    void resultUserYuntYield(String miningYield, String invoteYield, String childrenYield, String solarYield,
                             String galaxyYield, String groupYield, String clusterYield, String nodeYield,
                             String released, String unrelease, String b_released, String b_unrelease,
                             String h5Url);

    void resultInvoteConfig(String planetCount);

}
