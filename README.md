# quarkus-transactions Project

This project is for troubleshooting @Transactional behavior in Quarkus

* KafkaService listens on "commands-in" and sends the payload to MyService
* MyService gets the command 
** creates an Entity with the command payload
** persistes the Entity
** sends a new command out on another topic

Problemmatic behavior: 
* MyService cannot both persist the Entity and send a new message without throwing an exception
* MyService can send a message _or_ persist the entity with no problem
* Even with the exception the message gets sent
* The exception is

```
ARJUNA016039: onePhaseCommit on < formatId=131077, gtrid_length=35, bqual_length=36, tx_uid=0:ffff0a11059c:f7ad:617b081d:0, node_name=quarkus, branch_uid=0:ffff0a11059c:f7ad:617b081d:3, subordinatenodename=null, eis_name=0 > (io.agroal.narayana.LocalXAResource@34d8c4fd) failed with exception XAException.XA_RBROLLBACK: javax.transaction.xa.XAException: Error trying to transactionCommit local transaction: Enlisted connection used without active transaction
	at io.agroal.narayana.LocalXAResource.xaException(LocalXAResource.java:140)
	at io.agroal.narayana.LocalXAResource.xaException(LocalXAResource.java:134)
	at io.agroal.narayana.LocalXAResource.commit(LocalXAResource.java:72)
	at com.arjuna.ats.internal.jta.resources.arjunacore.XAResourceRecord.topLevelOnePhaseCommit(XAResourceRecord.java:702)
	at com.arjuna.ats.arjuna.coordinator.BasicAction.onePhaseCommit(BasicAction.java:2400)
	at com.arjuna.ats.arjuna.coordinator.BasicAction.End(BasicAction.java:1502)
	at com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator.end(TwoPhaseCoordinator.java:96)
	at com.arjuna.ats.arjuna.AtomicAction.commit(AtomicAction.java:162)
	at com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple.commitAndDisassociate(TransactionImple.java:1295)
	at com.arjuna.ats.internal.jta.transaction.arjunacore.BaseTransaction.commit(BaseTransaction.java:128)
	at io.quarkus.narayana.jta.runtime.CDIDelegatingTransactionManager.commit(CDIDelegatingTransactionManager.java:105)
	at io.quarkus.narayana.jta.runtime.CDIDelegatingTransactionManager_Subclass.commit$$superforward1(CDIDelegatingTransactionManager_Subclass.zig:386)
	at io.quarkus.narayana.jta.runtime.CDIDelegatingTransactionManager_Subclass$$function$$6.apply(CDIDelegatingTransactionManager_Subclass$$function$$6.zig:24)
	at io.quarkus.arc.impl.AroundInvokeInvocationContext.proceed(AroundInvokeInvocationContext.java:54)
	at io.quarkus.arc.runtime.devconsole.InvocationInterceptor.proceed(InvocationInterceptor.java:62)
	at io.quarkus.arc.runtime.devconsole.InvocationInterceptor.monitor(InvocationInterceptor.java:49)
	at io.quarkus.arc.runtime.devconsole.InvocationInterceptor_Bean.intercept(InvocationInterceptor_Bean.zig:516)
	at io.quarkus.arc.impl.InterceptorInvocation.invoke(InterceptorInvocation.java:41)
	at io.quarkus.arc.impl.AroundInvokeInvocationContext.perform(AroundInvokeInvocationContext.java:41)
	at io.quarkus.arc.impl.InvocationContexts.performAroundInvoke(InvocationContexts.java:32)
	at io.quarkus.narayana.jta.runtime.CDIDelegatingTransactionManager_Subclass.commit(CDIDelegatingTransactionManager_Subclass.zig:983)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorBase.endTransaction(TransactionalInterceptorBase.java:365)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorBase.invokeInOurTx(TransactionalInterceptorBase.java:169)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorBase.invokeInOurTx(TransactionalInterceptorBase.java:103)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorRequired.doIntercept(TransactionalInterceptorRequired.java:38)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorBase.intercept(TransactionalInterceptorBase.java:57)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorRequired.intercept(TransactionalInterceptorRequired.java:32)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorRequired_Bean.intercept(TransactionalInterceptorRequired_Bean.zig:335)
	at io.quarkus.arc.impl.InterceptorInvocation.invoke(InterceptorInvocation.java:41)
	at io.quarkus.arc.impl.AroundInvokeInvocationContext.perform(AroundInvokeInvocationContext.java:41)
	at io.quarkus.arc.impl.InvocationContexts.performAroundInvoke(InvocationContexts.java:32)
	at org.acme.infrastructure.KafkaService_Subclass.handleSomeKindOfCommand(KafkaService_Subclass.zig:221)
	at org.acme.infrastructure.KafkaService_ClientProxy.handleSomeKindOfCommand(KafkaService_ClientProxy.zig:128)
	at org.acme.infrastructure.KafkaService_SmallRyeMessagingInvoker_handleSomeKindOfCommand_5cee4639220937e9f2be8c2b93ec036155005a33.invoke(KafkaService_SmallRyeMessagingInvoker_handleSomeKindOfCommand_5cee4639220937e9f2be8c2b93ec036155005a33.zig:48)
	at io.smallrye.reactive.messaging.AbstractMediator.invoke(AbstractMediator.java:94)
	at io.smallrye.reactive.messaging.SubscriberMediator.lambda$processMethodReturningVoid$6(SubscriberMediator.java:169)
	at io.smallrye.context.impl.wrappers.SlowContextualSupplier.get(SlowContextualSupplier.java:21)
	at io.smallrye.mutiny.operators.uni.builders.UniCreateFromItemSupplier.subscribe(UniCreateFromItemSupplier.java:28)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:36)
	at io.smallrye.mutiny.operators.uni.UniOnItemOrFailureFlatMap.subscribe(UniOnItemOrFailureFlatMap.java:27)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:36)
	at io.smallrye.mutiny.converters.uni.UniToMultiPublisher$UniToMultiSubscription.request(UniToMultiPublisher.java:63)
	at io.smallrye.mutiny.operators.multi.MultiFlatMapOp$FlatMapInner.onSubscribe(MultiFlatMapOp.java:591)
	at io.smallrye.mutiny.converters.uni.UniToMultiPublisher.subscribe(UniToMultiPublisher.java:24)
	at io.smallrye.mutiny.groups.MultiCreate$1.subscribe(MultiCreate.java:156)
	at io.smallrye.mutiny.operators.multi.MultiFlatMapOp$FlatMapMainSubscriber.onItem(MultiFlatMapOp.java:191)
	at io.smallrye.mutiny.operators.multi.MultiMapOp$MapProcessor.onItem(MultiMapOp.java:50)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.subscription.SafeSubscriber.onNext(SafeSubscriber.java:98)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.operators.multi.MultiFlatMapOp$FlatMapMainSubscriber.tryEmit(MultiFlatMapOp.java:228)
	at io.smallrye.mutiny.operators.multi.MultiFlatMapOp$FlatMapInner.onItem(MultiFlatMapOp.java:597)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.converters.uni.UniToMultiPublisher$UniToMultiSubscription.onItem(UniToMultiPublisher.java:82)
	at io.smallrye.mutiny.operators.uni.UniOnItemTransformToUni$UniOnItemTransformToUniProcessor.onItem(UniOnItemTransformToUni.java:60)
	at io.smallrye.mutiny.operators.uni.builders.DefaultUniEmitter.complete(DefaultUniEmitter.java:36)
	at io.smallrye.mutiny.groups.UniOnNull.lambda$failWith$1(UniOnNull.java:43)
	at io.smallrye.context.impl.wrappers.SlowContextualBiConsumer.accept(SlowContextualBiConsumer.java:21)
	at io.smallrye.mutiny.groups.UniOnItem.lambda$transformToUni$4(UniOnItem.java:169)
	at io.smallrye.context.impl.wrappers.SlowContextualConsumer.accept(SlowContextualConsumer.java:21)
	at io.smallrye.mutiny.operators.uni.builders.UniCreateWithEmitter.subscribe(UniCreateWithEmitter.java:22)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:36)
	at io.smallrye.mutiny.operators.uni.UniOnItemTransformToUni$UniOnItemTransformToUniProcessor.performInnerSubscription(UniOnItemTransformToUni.java:81)
	at io.smallrye.mutiny.operators.uni.UniOnItemTransformToUni$UniOnItemTransformToUniProcessor.onItem(UniOnItemTransformToUni.java:57)
	at io.smallrye.mutiny.operators.uni.builders.UniCreateFromCompletionStage$CompletionStageUniSubscription.forwardResult(UniCreateFromCompletionStage.java:63)
	at java.base/java.util.concurrent.CompletableFuture.uniWhenComplete(CompletableFuture.java:859)
	at java.base/java.util.concurrent.CompletableFuture$UniWhenComplete.tryFire(CompletableFuture.java:837)
	at java.base/java.util.concurrent.CompletableFuture.postComplete(CompletableFuture.java:506)
	at java.base/java.uurrent.CompletableFuture.complete(CompletableFuture.java:2137)
	at io.quarkus.smallrye.reactivemessaging.runtime.devmode.DevModeSupportConnectorFactoryInterceptor.lambda$intercept$0(DevModeSupportConnectorFactoryInterceptor.java:42)
	at java.base/java.util.concurrent.CompletableFuture.uniWhenComplete(CompletableFuture.java:859)
	at java.base/java.util.concurrent.CompletableFuture$UniWhenComplete.tryFire(CompletableFuture.java:837)
	at java.base/java.util.concurrent.CompletableFuture.postComplete(CompletableFuture.java:506)
	at java.base/java.util.concurrent.CompletableFuture.complete(CompletableFuture.java:2137)
	at io.quarkus.smallrye.reactivemessaging.runtime.devmode.ReactiveMessagingHotReplacementSetup$OnMessage$1.run(ReactiveMessagingHotReplacementSetup.java:44)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:630)
	at java.base/java.lang.Thread.run(Thread.java:832)
Caused by: java.sql.SQLException: Enlisted connection used without active transaction
	at io.agroal.pool.ConnectionHandler.verifyEnlistment(ConnectionHandler.java:362)
	at io.agroal.pool.ConnectionHandler.transactionCommit(ConnectionHandler.java:331)
	at io.agroal.narayana.LocalXAResource.commit(LocalXAResource.java:69)
	... 77 more

16:29:18 ERROR [io.sm.re.me.provider] (pool-1-thread-1) SRMSG00200: The method org.acme.infrastructure.KafkaService#handleSomeKindOfCommand has thrown an exception: io.quarkus.arc.ArcUndeclaredThrowableException: Error invoking subclass method
	at org.acme.infrastructure.KafkaService_Subclass.handleSomeKindOfCommand(KafkaService_Subclass.zig:242)
	at org.acme.infrastructure.KafkaService_ClientProxy.handleSomeKindOfCommand(KafkaService_ClientProxy.zig:128)
	at org.acme.infrastructure.KafkaService_SmallRyeMessagingInvoker_handleSomeKindOfCommand_5cee4639220937e9f2be8c2b93ec036155005a33.invoke(KafkaService_SmallRyeMessagingInvoker_handleSomeKindOfCommand_5cee4639220937e9f2be8c2b93ec036155005a33.zig:48)
	at io.smallrye.reactive.messaging.AbstractMediator.invoke(AbstractMediator.java:94)
	at io.smallrye.reactive.messaging.SubscriberMediator.lambda$processMethodReturningVoid$6(SubscriberMediator.java:169)
	at io.smallrye.context.impl.wrappers.SlowContextualSupplier.get(SlowContextualSupplier.java:21)
	at io.smallrye.mutiny.operators.uni.builders.UniCreateFromItemSupplier.subscribe(UniCreateFromItemSupplier.java:28)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:36)
	at io.smallrye.mutiny.operators.uni.UniOnItemOrFailureFlatMap.subscribe(UniOnItemOrFailureFlatMap.java:27)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:36)
	at io.smallrye.mutiny.converters.uni.UniToMultiPublisher$UniToMultiSubscription.request(UniToMultiPublisher.java:63)
	at io.smallrye.mutiny.operators.multi.MultiFlatMapOp$FlatMapInner.onSubscribe(MultiFlatMapOp.java:591)
	at io.smallrye.mutiny.converters.uni.UniToMultiPublisher.subscribe(UniToMultiPublisher.java:24)
	at io.smallrye.mutiny.groups.MultiCreate$1.subscribe(MultiCreate.java:156)
	at io.smallrye.mutiny.operators.multi.MultiFlatMapOp$FlatMapMainSubscriber.onItem(MultiFlatMapOp.java:191)
	at io.smallrye.mutiny.operators.multi.MultiMapOp$MapProcessor.onItem(MultiMapOp.java:50)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.subscription.SafeSubscriber.onNext(SafeSubscriber.java:98)
	at io.smallrye.mutiny.helpers.HalfSerializer.onNext(HalfSerializer.java:31)
	at io.smallrye.mutiny.helpers.StrictMultiSubscriber.onItem(StrictMultiSubscriber.java:83)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.operators.multi.MultiFlatMapOp$FlatMapMainSubscriber.tryEmit(MultiFlatMapOp.java:228)
	at io.smallrye.mutiny.operators.multi.MultiFlatMapOp$FlatMapInner.onItem(MultiFlatMapOp.java:597)
	at io.smallrye.mutiny.subscription.MultiSubscriber.onNext(MultiSubscriber.java:61)
	at io.smallrye.mutiny.converters.uni.UniToMultiPublisher$UniToMultiSubscription.onItem(UniToMultiPublisher.java:82)
	at io.smallrye.mutiny.operators.uni.UniOnItemTransformToUni$UniOnItemTransformToUniProcessor.onItem(UniOnItemTransformToUni.java:60)
	at io.smallrye.mutiny.operators.uni.builders.DefaultUniEmitter.complete(DefaultUniEmitter.java:36)
	at io.smallrye.mutiny.groups.UniOnNull.lambda$failWith$1(UniOnNull.java:43)
	at io.smallrye.context.impl.wrappers.SlowContextualBiConsumer.accept(SlowContextualBiConsumer.java:21)
	at io.smallrye.mutiny.groups.UniOnItem.lambda$transformToUni$4(UniOnItem.java:169)
	at io.smallrye.context.impl.wrappers.SlowContextualConsumer.accept(SlowContextualConsumer.java:21)
	at io.smallrye.mutiny.operators.uni.builders.UniCreateWithEmitter.subscribe(UniCreateWithEmitter.java:22)
	at io.smallrye.mutiny.operators.AbstractUni.subscribe(AbstractUni.java:36)
	at io.smallrye.mutiny.operators.uni.UniOnItemTransformToUni$UniOnItemTransformToUniProcessor.performInnerSubscription(UniOnItemTransformToUni.java:81)
	at io.smallrye.mutiny.operators.uni.UniOnItemTransformToUni$UniOnItemTransformToUniProcessor.onItem(UniOnItemTransformToUni.java:57)
	at io.smallrye.mutiny.operators.uni.builders.UniCreateFromCompletionStage$CompletionStageUniSubscription.forwardResult(UniCreateFromCompletionStage.java:63)
	at java.base/java.util.concurrent.CompletableFuture.uniWhenComplete(CompletableFuture.java:859)
	at java.base/java.util.concurrent.CompletableFuture$UniWhenComplete.tryFire(CompletableFuture.java:837)
	at java.base/java.util.concurrent.CompletableFuture.postComplete(CompletableFuture.java:506)
	at java.base/java.util.concurrent.CompletableFuture.complete(CompletableFuture.java:2137)
	at io.quarkus.smallrye.reactivemessaging.runtime.devmode.DevModeSupportConnectorFactoryInterceptor.lambda$intercept$0(DevModeSupportConnectorFactoryInterceptor.java:42)
	at java.base/java.util.concurrent.CompletableFuture.uniWhenComplete(CompletableFuture.java:859)
	at java.base/java.util.concurrent.CompletableFuture$UniWhenComplete.tryFire(CompletableFuture.java:837)
	at java.base/java.util.concurrent.CompletableFuture.postComplete(CompletableFuture.java:506)
	at java.base/java.util.concurrent.CompletableFuture.complete(CompletableFuture.java:2137)
	at io.quarkus.smallrye.reactivemessaging.runtime.devmode.ReactiveMessagingHotReplacementSetup$OnMessage$1.run(ReactiveMessagingHotReplacementSetup.java:44)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:630)
	at java.base/java.lang.Thread.run(Thread.java:832)
Caused by: javax.transaction.RollbackException: ARJUNA016053: Could not commit transaction.
	at com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple.commitAndDisassociate(TransactionImple.java:1307)
	at com.arjuna.ats.internal.jta.transaction.arjunacore.BaseTransaction.commit(BaseTransaction.java:128)
	at io.quarkus.narayana.jta.runtime.CDIDelegatingTransactionManager.commit(CDIDelegatingTransactionManager.java:105)
	at io.quarkus.narayana.jta.runtime.CDIDelegatingTransactionManager_Subclass.commit$$superforward1(CDIDelegatingTransactionManager_Subclass.zig:386)
	at io.quarkus.narayana.jta.runtime.CDIDelegatingTransactionManager_Subclass$$function$$6.apply(CDIDelegatingTransactionManager_Subclass$$function$$6.zig:24)
	at io.quarkus.arc.impl.AroundInvokeInvocationContext.proceed(AroundInvokeInvocationContext.java:54)
	at io.quarkus.arc.runtime.devconsole.InvocationInterceptor.proceed(InvocationInterceptor.java:62)
	at io.quarkus.arc.runtime.devconsole.InvocationInterceptor.monitor(InvocationInterceptor.java:49)
	at io.quarkus.arc.runtime.devconsole.InvocationInterceptor_Bean.intercept(InvocationInterceptor_Bean.zig:516)
	at io.quarkus.arc.impl.InterceptorInvocation.invoke(InterceptorInvocation.java:41)
	at io.quarkus.arc.impl.AroundInvokeInvocationContext.perform(AroundInvokeInvocationContext.java:41)
	at io.quarkus.arc.impl.InvocationContexts.performAroundInvoke(InvocationContexts.java:32)
	at io.quarkus.narayana.jta.runtime.CDIDelegatingTransactionManager_Subclass.commit(CDIDelegatingTransactionManager_Subclass.zig:983)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorBase.endTransaction(TransactionalInterceptorBase.java:365)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorBase.invokeInOurTx(TransactionalInterceptorBase.java:169)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorBase.invokeInOurTx(TransactionalInterceptorBase.java:103)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorRequired.doIntercept(TransactionalInterceptorRequired.java:38)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorBase.intercept(TransactionalInterceptorBase.java:57)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorRequired.intercept(TransactionalInterceptorRequired.java:32)
	at io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorRequired_Bean.intercept(TransactionalInterceptorRequired_Bean.zig:335)
	at io.quarkus.arc.impl.InterceptorInvocation.invoke(Interceptoron.java:41)
	at io.quarkus.arc.impl.AroundInvokeInvocationContext.perform(AroundInvokeInvocationContext.java:41)
	at io.quarkus.arc.impl.InvocationContexts.performAroundInvoke(InvocationContexts.java:32)
	at org.acme.infrastructure.KafkaService_Subclass.handleSomeKindOfCommand(KafkaService_Subclass.zig:221)
	... 48 more
	Suppressed: javax.transaction.xa.XAException: Error trying to transactionCommit local transaction: Enlisted connection used without active transaction
		at io.agroal.narayana.LocalXAResource.xaException(LocalXAResource.java:140)
		at io.agroal.narayana.LocalXAResource.xaException(LocalXAResource.java:134)
		at io.agroal.narayana.LocalXAResource.commit(LocalXAResource.java:72)
		at com.arjuna.ats.internal.jta.resources.arjunacore.XAResourceRecord.topLevelOnePhaseCommit(XAResourceRecord.java:702)
		at com.arjuna.ats.arjuna.coordinator.BasicAction.onePhaseCommit(BasicAction.java:2400)
		at com.arjuna.ats.arjuna.coordinator.BasicAction.End(BasicAction.java:1502)
		at com.arjuna.ats.arjuna.coordinator.TwoPhaseCoordinator.end(TwoPhaseCoordinator.java:96)
		at com.arjuna.ats.arjuna.AtomicAction.commit(AtomicAction.java:162)
		at com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionImple.commitAndDisassociate(TransactionImple.java:1295)
		... 71 more
	Caused by: java.sql.SQLException: Enlisted connection used without active transaction
		at io.agroal.pool.ConnectionHandler.verifyEnlistment(ConnectionHandler.java:362)
		at io.agroal.pool.ConnectionHandler.transactionCommit(ConnectionHandler.java:331)
		at io.agroal.narayana.LocalXAResource.commit(LocalXAResource.java:69)
		... 77 more
```