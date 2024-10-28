package com.ckdwls.boardguide.Service;

public class HaversineUtil {
    
    private static final double EARTH_RADIUS = 6371; // 지구 반지름 (킬로미터)

    // 두 좌표 사이의 거리를 계산하는 메서드
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // 킬로미터 단위로 반환
    }
}