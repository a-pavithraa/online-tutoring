package com.studentassessment.feign;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import java.util.Set;


public class FeignErrorDecoder extends ErrorDecoder.Default {

  private final Set<Integer> retryableStatusCodes;

  /** */
  public FeignErrorDecoder(Set<Integer> retryableStatusCodes) {
    this.retryableStatusCodes = retryableStatusCodes;
  }

  @Override
  public Exception decode(String methodKey, Response response) {
    // Default error decoder converts response to either FeignException or RetryableException(if
    // Retry-After header is present in response).

    Exception ex = super.decode(methodKey, response);
    if (ex instanceof RetryableException) {
      return ex;
    } else if (ex instanceof FeignException) {
      FeignException feignException = (FeignException) ex;
      if (retryableStatusCodes.contains(feignException.status())) {
        return new RetryableException(
            0, feignException.getMessage(), response.request().httpMethod(), feignException, null, null);
      }
      return feignException;
    }
    return ex;
  }
}
