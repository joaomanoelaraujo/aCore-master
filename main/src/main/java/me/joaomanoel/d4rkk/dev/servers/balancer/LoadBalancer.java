package me.joaomanoel.d4rkk.dev.servers.balancer;

import me.joaomanoel.d4rkk.dev.servers.balancer.elements.LoadBalancerObject;

public interface LoadBalancer<T extends LoadBalancerObject> {
  T next();
}
