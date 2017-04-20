package io.featureflow.client;

import io.featureflow.client.model.Feature;
import io.featureflow.client.model.Variant;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by oliver on 26/05/2016.
 */
public class FeatureflowClientImplTestInt {

    FeatureflowClient featureflowClient;
    private CountDownLatch lock = new CountDownLatch(100);
    @Test
    public void testEvaluate() throws Exception {


        FeatureflowConfig config = FeatureflowConfig.builder()
                .withBaseUri(TestConfiguration.LOCAL_BASE_URL)
                .withStreamBaseUri(TestConfiguration.LOCAL_BASE_STREAM_URL)
                //.withOffline(true)
                .withWaitForStartup(5000l)
                .build();

        FeatureflowContext context = FeatureflowContext.keyedContext("uniqueuserkey1")
                .withValue("tier", "silver")
                .withValue("age", 32)
                .withValue("signup_date", new DateTime(2017, 1, 1, 12, 0, 0, 0))
                .withValue("name", "Oliver Oldfield-Hodge")
                .withValue("email", "oliver@featureflow.io")
                .withValues("user_role", Arrays.asList("pvt_tester", "administrator"))
                .build();


        featureflowClient = new
                //FF LOCAL OLIS LOCAL
                FeatureflowClient.Builder("")
                .withConfig(config)
                .withFeatures(Arrays.asList(
                        new Feature(FeatureKeys.alpha.name()),
                        new Feature(FeatureKeys.beta.name()),
                        new Feature(FeatureKeys.manage.name()),
                        new Feature(FeatureKeys.events.name()),
                        new Feature(FeatureKeys.analytics.name()),
                        new Feature(FeatureKeys.experiments.name()),
                        new Feature(FeatureKeys.billing.name()),
                        new Feature(FeatureKeys.stripe.name())

                ))
                .withUpdateCallback(control -> System.out.println("Received a control update event: " + control.getKey()))
                .withUpdateCallback(control -> {
                    System.out.println("Feature updated: " + control.getKey() + " - variant: " + control.evaluate(context) + "\n");
                    lock.countDown();
                    System.out.println(featureflowClient.evaluate(FeatureKeys.billing.name()).isOff());
                })
                .withConfig(config).build();
        String evaluatedVariant = featureflowClient.evaluate("example-feature", context).value();
        System.out.println(featureflowClient.evaluate(FeatureKeys.billing.name()).value());
        System.out.println(evaluatedVariant);
        lock.await(500000, TimeUnit.MILLISECONDS);

        System.out.println(featureflowClient.evaluate("alpha", context));
    }
}