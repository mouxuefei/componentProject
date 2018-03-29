package com.sihaiwanlian.baseproject.utils;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.orhanobut.logger.Logger;
import com.sihaiwanlian.baselib.base.LibApplication;
import com.sihaiwanlian.baselib.utils.ToastBottomUtils;

/**
 * Created by mou on 2017/11/6
 */

public class GaoDe2GPSUtils {

    /**
     * GPS坐标转高德坐标
     */
    public static DPoint getGDFromGPS(double latitude, double longitude) {
        CoordinateConverter converter = new CoordinateConverter(LibApplication.Companion.getMContext());
        converter.from(CoordinateConverter.CoordType.GPS);
        DPoint desLatLng = null;
        try {
            converter.coord(new DPoint(latitude, longitude));
            desLatLng = converter.convert();
            Logger.e("desLatLng.getLatitude()=" + desLatLng.getLatitude() + "desLatLng.getLongitude()=" + desLatLng.getLongitude());
        } catch (Exception e) {
            ToastBottomUtils.showToastBottom("GPS坐标解析错误");
        }
        return desLatLng;
    }

}
