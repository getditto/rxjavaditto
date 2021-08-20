package com.ncorti.kotlin.template.library.android
import io.reactivex.rxjava3.core.Observable

import live.ditto.*


data class DittoPendingCursorOperationSnapshot(val documents: List<DittoDocument>, val liveQueryEvent: DittoLiveQueryEvent)

fun DittoPendingCursorOperation.asObservable(): Observable<DittoPendingCursorOperationSnapshot> {
    return Observable.create { s ->
        val liveQuery = this.observe { docs, dittoLiveQueryEvent ->
            val snapshot = DittoPendingCursorOperationSnapshot(docs, dittoLiveQueryEvent)
            s.onNext(snapshot)
        }
        s.setCancellable {
            liveQuery.stop()
        }
    }
}

data class DittoPendingIDSpecificOperationSnapshot(val document: DittoDocument?, val liveQueryEvent: DittoSingleDocumentLiveQueryEvent)

fun DittoPendingIDSpecificOperation.asObservable(): Observable<DittoPendingIDSpecificOperationSnapshot> {
    return Observable.create { s ->
        val liveQuery = this.observe { doc, dittoLiveQueryEvent ->
            val snapshot = DittoPendingIDSpecificOperationSnapshot(doc, dittoLiveQueryEvent)
            s.onNext(snapshot)
        }
        s.setCancellable {
            liveQuery.stop()
        }
    }
}

fun Ditto.peersAsObservable(): Observable<List<DittoRemotePeer>> {
    return Observable.create { subscriber ->
        val handler = this.observePeers { peers ->
            subscriber.onNext(peers)
        }
        subscriber.setCancellable {
            handler.close()
        }
    }
}