import SwiftUI
import shared

func greet() -> String {
    return Greeting().greeting()
}

struct ContentView: View {
    var body: some View {
        NavigationView {
            List {
                Text("Top manga")
                Text("Top novel")
                Text("Top anime")
            }.navigationBarTitle("Browse")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
