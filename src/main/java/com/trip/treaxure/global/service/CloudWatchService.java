package com.trip.treaxure.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CloudWatchService {

    private final CloudWatchClient cloudWatchClient;
    private static final String NAMESPACE = "Treaxure/Backend";

    /**
     * CloudWatch에 업로드 이벤트 메트릭 로깅
     * @param userNickname 사용자 닉네임
     * @param fileType 파일 타입
     */
    public void logUploadEvent(String userNickname, String fileType) {
        try {
            Dimension userDimension = Dimension.builder()
                    .name("UserNickname")
                    .value(userNickname)
                    .build();

            Dimension typeDimension = Dimension.builder()
                    .name("FileType")
                    .value(fileType)
                    .build();

            MetricDatum datum = MetricDatum.builder()
                    .metricName("ImageUpload")
                    .unit(StandardUnit.COUNT)
                    .value(1.0)
                    .timestamp(Instant.now())
                    .dimensions(userDimension, typeDimension)
                    .build();

            PutMetricDataRequest request = PutMetricDataRequest.builder()
                    .namespace(NAMESPACE)
                    .metricData(datum)
                    .build();

            cloudWatchClient.putMetricData(request);
        } catch (Exception e) {
            // 로깅 실패 시 애플리케이션 중단을 방지하기 위해 예외 처리
            System.err.println("CloudWatch 메트릭 로깅 실패: " + e.getMessage());
        }
    }

    /**
     * CloudFront 액세스 이벤트 로깅
     * @param userNickname 사용자 닉네임
     * @param accessType 액세스 타입 (URL or Cookie)
     */
    public void logCloudFrontAccess(String userNickname, String accessType) {
        try {
            Dimension userDimension = Dimension.builder()
                    .name("UserNickname")
                    .value(userNickname)
                    .build();

            Dimension typeDimension = Dimension.builder()
                    .name("AccessType")
                    .value(accessType)
                    .build();

            MetricDatum datum = MetricDatum.builder()
                    .metricName("CloudFrontAccess")
                    .unit(StandardUnit.COUNT)
                    .value(1.0)
                    .timestamp(Instant.now())
                    .dimensions(userDimension, typeDimension)
                    .build();

            PutMetricDataRequest request = PutMetricDataRequest.builder()
                    .namespace(NAMESPACE)
                    .metricData(datum)
                    .build();

            cloudWatchClient.putMetricData(request);
        } catch (Exception e) {
            System.err.println("CloudWatch 메트릭 로깅 실패: " + e.getMessage());
        }
    }
} 