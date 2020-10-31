//
//  AnimeListView.swift
//  iosApp
//
//  Created by Coder on 10/31/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct AnimeListView: View {
    @ObservedObject var vm: MoreAnimeVM
    private let title: String
    
    init(listUI: AnimeListUI) {
        vm = MoreAnimeVM(listUI: listUI)
        title = listUI.title
    }
    
    var body: some View {
        List {
            ForEach(0..<vm.list.count, id: \.self) { index in
                HStack {
                    ForEach(vm.list[index]) { animeUI in
                        AnimeView(item: animeUI)
                            .onAppear {
                                let itemId = animeUI.id
                                vm.onItemAppear(itemId: itemId)
                            }
                    }
                }
            }
        }
        .navigationBarTitle(Text("\(title)"), displayMode: .inline)
    }
}
