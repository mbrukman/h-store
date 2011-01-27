/***************************************************************************
 *   Copyright (C) 2011 by H-Store Project                                 *
 *   Brown University                                                      *
 *   Massachusetts Institute of Technology                                 *
 *   Yale University                                                       *
 *                                                                         *
 *   Permission is hereby granted, free of charge, to any person obtaining *
 *   a copy of this software and associated documentation files (the       *
 *   "Software"), to deal in the Software without restriction, including   *
 *   without limitation the rights to use, copy, modify, merge, publish,   *
 *   distribute, sublicense, and/or sell copies of the Software, and to    *
 *   permit persons to whom the Software is furnished to do so, subject to *
 *   the following conditions:                                             *
 *                                                                         *
 *   The above copyright notice and this permission notice shall be        *
 *   included in all copies or substantial portions of the Software.       *
 *                                                                         *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,       *
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF    *
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.*
 *   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR     *
 *   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, *
 *   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR *
 *   OTHER DEALINGS IN THE SOFTWARE.                                       *
 ***************************************************************************/
package edu.mit.hstore.dtxn;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.log4j.Logger;
import org.voltdb.ExecutionSite;
import org.voltdb.VoltTable;

import edu.brown.utils.LoggerUtil;

public class RemoteTransactionState extends TransactionState {
    protected static final Logger LOG = Logger.getLogger(RemoteTransactionState.class);
    private final static AtomicBoolean debug = new AtomicBoolean(LOG.isDebugEnabled());
    private final static AtomicBoolean trace = new AtomicBoolean(LOG.isTraceEnabled());
    static {
        LoggerUtil.attachObserver(LOG, debug, trace);
    }
    
    /**
     * RemoteTransactionState Factory
     */
    public static class Factory extends BasePoolableObjectFactory {
        private final ExecutionSite executor;
        
        public Factory(ExecutionSite executor) {
            this.executor = executor;
        }
        @Override
        public Object makeObject() throws Exception {
            return new RemoteTransactionState(this.executor);
        }
    };
    
    public RemoteTransactionState(ExecutionSite executor) {
        super(executor);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public RemoteTransactionState init(long txnId, long clientHandle, int source_partition) {
        return ((RemoteTransactionState)super.init(txnId, clientHandle, source_partition, false));
    }
    
    @Override
    public void initRound(long undoToken) {
        super.initRound(undoToken);
    }
    
    @Override
    public void startRound() {
        // If the stored procedure is not executing locally then we need at least
        // one FragmentTaskMessage callback
        assert(this.fragment_callbacks.isEmpty() == false) :
            "No FragmentTaskMessage callbacks available for txn #" + this.txn_id;
        super.startRound();
    }
    
    @Override
    public void finishRound() {
        super.finishRound();
    }
    
    @Override
    public VoltTable[] getResults() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void addResponse(int partition, int dependencyId) {
        // Nothing
    }
    
    @Override
    public void addResult(int partition, int dependencyId, VoltTable result) {
        // Nothing
    }
}
