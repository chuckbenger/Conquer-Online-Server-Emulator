using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;

namespace BlackBeltCO.COSocket
{
    //Callback for when queued data needs to be processed
    public delegate void handleCallback(object data);

    public class ConsumerPool : IDisposable 
    {
        
        private object locker = new object();
        private Thread[] workers;
        private Queue<object> tasks = new Queue<object>();
        private handleCallback _callback;


        /// <summary>
        /// Creates a new pool of worker threads to handle queued data
        /// </summary>
        /// <param name="workers">Leave blank to auto choose best amount</param>
        public ConsumerPool( handleCallback callback, int workerCount = 0)
        {
            this._callback = callback;

            //Create 2 time the amount of processors
            if (workerCount == 0)
                workerCount = Environment.ProcessorCount * 2;

            workers = new Thread[workerCount];

            for (short i = 0; i < workerCount; i++)
            {
                workers[i] = new Thread(new ThreadStart(process));
                workers[i].Start();
            }

            //Gives threads some time to start
            Thread.Sleep(50);
        }

        /// <summary>
        /// Enqueues a null to each thread to stop processing 
        /// and joins them.
        /// </summary>
        public void Dispose()
        {
            foreach (Thread worker in workers) addTask(null);
            foreach (Thread worker in workers) worker.Join();
        }

        /// <summary>
        /// Adds the task the queue for processing
        /// </summary>
        /// <param name="task">The task to add</param>
        public void addTask(object task)
        {
            lock (locker)
            {
                tasks.Enqueue(task);
                Monitor.PulseAll(locker);
            }
        }

        /// <summary>
        /// Processes queued items until a null item is returned
        /// </summary>
        private void process()
        {
            while (true)
            {
                object task;
                lock (locker)
                {
                    //Wait until we have a task
                    while (tasks.Count == 0) Monitor.Wait(locker);
                    task = tasks.Dequeue();
                }
                if (task == null)
                    return;
                
                //Handle the task
                _callback(task);
            }
        }
    }
}
