using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;

namespace BlackBeltCO.COSocket
{

    /// <summary>
    /// Consumer pool provides a class for processing queued data the relates 
    /// to a specific object that implements IWorkable
    /// </summary>
    public class ConsumerPool : IDisposable 
    {
        
        private object locker = new object(); //Used for locking

        private Thread[] workers; //Worker threads
      


        //Holds a dictionary of known ClientSockets and each client socket set holds an queue of packets
        private Dictionary<IWorkable, Queue<object>> tasks = new Dictionary<IWorkable, Queue<object>>();

        /// <summary>
        /// Creates a new pool of worker threads to handle queued data
        /// </summary>
        /// <param name="workers">Leave blank to auto choose best amount</param>
        public ConsumerPool(int workerCount = 0)
        {
            //Create 2 time the amount of processors of threads
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
            //Indicate to remove that client from the dictionary
            foreach (IWorkable client in tasks.Keys)
                addTask(client, null);
        }

        /// <summary>
        /// Adds the task the queue for processing
        /// </summary>
        /// <param name="task">The task to add</param>
        public void addTask(IWorkable client, object task)
        {
           
            lock (locker)
            {
                Queue<object> queue;

                //Attempt to find a queue for the passed in client
                //and if one doesn't exist create one and associate it
                //with that client
                if (!tasks.TryGetValue(client, out queue))
                {
                    queue = new Queue<object>();
                    tasks.Add(client, queue);
                }

                //Add the task
                queue.Enqueue(task);
               
                //Tell consumer threads to wake up
                Monitor.PulseAll(locker);
            }
        }

        /// <summary>
        /// Remove the specified key from the dictionary
        /// </summary>
        /// <param name="worker">The worker item to remove</param>
        public void removeKey(IWorkable worker)
        {
            addTask(worker, null);
        }

        /// <summary>
        /// Processes queued items until a null item is returned.
        /// Loops through each IWorkable object and checks if there
        /// queue has data. If so it's processed other wise it's not.
        /// 
        /// Each pass of the inner loop processes exactly 1 packet from each client and moves
        /// on to the next to prevent 1 user from taking up lots of CPU time
        /// </summary>
        private void process()
        {
            while (true)
            {
                object task = null; //The object to process
                bool empty = true;  //Whether or not all queues are empty
                IWorkable itemToRemove = null; //Worker that should be removed from the dictionary

                lock (locker)
                {
                    //Wait until we have a task
                    while (tasks.Count == 0) Monitor.Wait(locker);

                    foreach (IWorkable client in tasks.Keys)
                    {
                        Queue<object> data;

                        if (tasks.TryGetValue(client, out data))
                        {
                            if (data.Count > 0)
                            {
                                empty = false;
                                task = data.Dequeue();

                                //If null client disconnected so remove them
                                if (task == null)
                                    itemToRemove = client;
                                else
                                    client.Handler.Handle(task);
                            }
                        } 
                    }

                    //Remove item if needed
                    if (itemToRemove != null)
                        tasks.Remove(itemToRemove);

                    //If all queues empty pause the thread
                    if (empty)
                        Monitor.Wait(locker);
                }
                
            }
        }
    }
}
