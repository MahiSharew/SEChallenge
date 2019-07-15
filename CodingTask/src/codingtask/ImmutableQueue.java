/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codingtask;

/**
 *
 * @author mahlet
 */
/**
 * We can implement immutable queue using two immutable stack
 *
 */
public final class ImmutableQueue<T> implements Queue<T> {

    /**
     *
     * we create private immutable stack class that can only access inside
     * ImmutableQueue class
     */
    private class IStack {

        private final T head;
        private final IStack tail;

        public IStack() {
            this.head = null;
            this.tail = null;
        }

        private IStack(T head, IStack tail) {
            this.head = head;
            this.tail = tail;
        }

        public IStack push(T t) {
            return new IStack(t, this);
        }

        public IStack pop() {
            return this.tail;
        }

        public T peek() {
            return this.head;
        }

        public boolean isEmpty() {
            return this.head == null && this.tail == null;
        }

        public IStack reverse() {
            IStack result = new IStack();
            IStack tmp = this;
            while (!tmp.isEmpty()) {
                result = result.push(tmp.peek());
                tmp = tmp.pop();
            }
            return result;
        }
    }

  
    private final IStack stack1; // stack to remove elements from
    private final IStack stack2; // stack to add elements to

    public ImmutableQueue() {
        this.stack1 = new IStack();
        this.stack2 = new IStack();
    }

    private ImmutableQueue(IStack stack1, IStack stack2) {
        this.stack1 = stack1;
        this.stack2 = stack2;
    }

    /**
     * enQueue(T t ) check if stack1 is empty reverse stack2 and push the new
     * element to stack1 and return the new Queue instance 
     *
     */
    @Override
    public Queue<T> enQueue(T t) {
        if (this.stack1.isEmpty()) {
            return new ImmutableQueue<>(this.stack2.reverse().push(t), new IStack());
        }
        return new ImmutableQueue<>(this.stack1, this.stack2.push(t));
    }

    /**
     * deQueue() check if stack1 is empty reverse stack2 ,pop its top and set it
      as stack1 stack Queue instance      
     *
     */
    @Override
    public Queue<T> deQueue() {
        if (this.stack1.isEmpty()) {
            return new ImmutableQueue<>(this.stack2.reverse().pop(), new IStack());
        } else {
            IStack newFront = this.stack1.pop();
            if (newFront.isEmpty()) {

                return new ImmutableQueue<>(this.stack2.reverse(), new IStack());
            }
            return new ImmutableQueue<>(newFront, this.stack2);
        }
    }

    @Override
    public T head() {
        return this.stack1.peek();
    }

    @Override
    public boolean isEmpty() {
        return this.stack1.isEmpty() && this.stack2.isEmpty();
    }
}
