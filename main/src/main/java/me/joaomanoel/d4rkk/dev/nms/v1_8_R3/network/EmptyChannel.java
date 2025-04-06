package me.joaomanoel.d4rkk.dev.nms.v1_8_R3.network;

import io.netty.channel.*;

import java.net.SocketAddress;

public class EmptyChannel extends AbstractChannel {
  
  public EmptyChannel() {
    super(null);
  }
  
  @Override
  public ChannelConfig config() {
    return null;
  }
  
  @Override
  public boolean isActive() {
    return false;
  }
  
  @Override
  public boolean isOpen() {
    return false;
  }
  
  @Override
  public ChannelMetadata metadata() {
    return null;
  }
  
  @Override
  protected void doBeginRead() throws Exception {
  }
  
  @Override
  protected void doBind(SocketAddress arg0) throws Exception {
  }
  
  @Override
  protected void doClose() throws Exception {
  }
  
  @Override
  protected void doDisconnect() throws Exception {
  }
  
  @Override
  protected void doWrite(ChannelOutboundBuffer arg0) throws Exception {
  }
  
  @Override
  protected boolean isCompatible(EventLoop arg0) {
    return false;
  }
  
  @Override
  protected SocketAddress localAddress0() {
    return null;
  }
  
  @Override
  protected AbstractUnsafe newUnsafe() {
    return null;
  }
  
  @Override
  protected SocketAddress remoteAddress0() {
    return null;
  }
}
