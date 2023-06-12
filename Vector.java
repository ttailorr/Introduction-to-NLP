public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        //TODO Task 1.1
        doubElements = _elements;
    }

    public double getElementatIndex(int _index) {
        //TODO Task 1.2
        if(_index >= doubElements.length){
            return -1;
        }
        return doubElements[_index];
    }

    public void setElementatIndex(double _value, int _index) {
        //TODO Task 1.3
        if(_index >= doubElements.length){
            doubElements[doubElements.length-1] = _value;
        }
        else{
            doubElements[_index] = _value;
        }
    }

    public double[] getAllElements() {
        //TODO Task 1.4
        return doubElements;
    }

    public int getVectorSize() {
        //TODO Task 1.5
        return doubElements.length;
    }

    public Vector reSize(int _size) {
        //TODO Task 1.6

        if(_size == this.doubElements.length || _size <= 0){
            return new Vector(this.getAllElements());
        }
        else if (_size < this.doubElements.length) {
            double[] arr = new double[_size];
            for(int i = 0; i < _size; i++){
                arr[i] = this.doubElements[i];
            }
            return new Vector(arr);
        }
        else{
            /*
            create new array of size: _size
            create a for loop with an if else statement iterating through the array
            if current index < doubElement.length then assign value from doubElement to index at new array
            else assign all remaining elements in array to -1.0
             */
            double[] arr = new double[_size];
            for(int i = 0; i < _size; i++){
                if(i < this.doubElements.length){
                    arr[i] = this.doubElements[i];
                }
                else{
                    arr[i] = -1.0;
                }
            }
            return new Vector(arr);
        }

    }

    public Vector add(Vector _v) {
        //TODO Task 1.7
        Vector newVector = new Vector(doubElements);
        if(_v.getAllElements().length < this.doubElements.length){
            _v = _v.reSize(this.doubElements.length);
        }
        else if(this.doubElements.length < _v.getAllElements().length){
            newVector = newVector.reSize(_v.getAllElements().length);
        }

        for(int i = 0; i < _v.getAllElements().length; i++){
            newVector.setElementatIndex(newVector.getElementatIndex(i) + _v.getElementatIndex(i), i);
        }

        return newVector;
    }

    public Vector subtraction(Vector _v) {
        //TODO Task 1.8
        Vector newVector = new Vector(doubElements);
        if(_v.getAllElements().length < this.doubElements.length){
            _v = _v.reSize(this.doubElements.length);
        }
        else if(this.doubElements.length < _v.getAllElements().length){
            newVector = newVector.reSize(_v.getAllElements().length);
        }

        for(int i = 0; i < _v.getAllElements().length; i++){
            newVector.setElementatIndex(newVector.getElementatIndex(i) - _v.getElementatIndex(i), i);
        }
        return newVector;
    }

    public double dotProduct(Vector _v) {
        //TODO Task 1.9
        Vector newVector = new Vector(doubElements);
        if(_v.getAllElements().length < this.doubElements.length){
            _v = _v.reSize(this.doubElements.length);
        }
        else if(this.doubElements.length < _v.getAllElements().length){
            newVector = newVector.reSize(_v.getAllElements().length);
        }

        double dproduct = 0.0;
        for(int i = 0; i < _v.getAllElements().length; i++){
            dproduct += _v.getElementatIndex(i) * newVector.getElementatIndex(i);
        }
        return dproduct;
    }

    public double cosineSimilarity(Vector _v) {
        //TODO Task 1.10
        Vector newVector = new Vector(doubElements);
        if(_v.getAllElements().length < this.doubElements.length){
            _v = _v.reSize(this.doubElements.length);
        }
        else if(this.doubElements.length < _v.getAllElements().length){
            newVector = newVector.reSize(_v.getAllElements().length);
        }

        double dproduct = dotProduct(_v);
        double sigmaA = 0.0;
        double sigmaB = 0.0;
        for (int i = 0; i < _v.getAllElements().length; i++) {
            sigmaA += Math.pow(newVector.getElementatIndex(i), 2);
            sigmaB += Math.pow(_v.getElementatIndex(i), 2);
        }
        return dproduct / (Math.sqrt(sigmaA) * Math.sqrt(sigmaB));
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;
        //TODO Task 1.11
        if(v.getAllElements().length != this.doubElements.length){
            return false;
        }
        for(int i = 0; i < doubElements.length; i++){
            if(v.getElementatIndex(i) != this.doubElements[i]){
                boolEquals = false;
            }
        }
        return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}
