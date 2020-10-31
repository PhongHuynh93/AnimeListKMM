//
//  MangaListView.swift
//  iosApp
//
//  Created by Coder on 10/18/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct MangaListView: View {
    @ObservedObject var vm: MoreMangaVM
    private let title: String
    
    init(mangaListUI: MangaListUI) {
        vm = MoreMangaVM(mangaListUI: mangaListUI)
        title = mangaListUI.title
    }
    
    var body: some View {
        List {
            ForEach(0..<vm.list.count, id: \.self) { index in
                HStack {
                    ForEach(vm.list[index]) { mangaUI in
                        MangaView(mangaUI: mangaUI)
                            .onAppear {
                                let itemId = mangaUI.id
                                vm.onItemAppear(itemId: itemId)
                            }
                    }
                }
            }
        }
        .navigationBarTitle(Text("\(title)"), displayMode: .inline)
    }
}
